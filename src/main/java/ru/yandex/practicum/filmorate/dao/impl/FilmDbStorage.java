package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    @Override
    public List<Film> findAll() {
        String sqlQuery = "select FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, m.MPA_ID as MPA_ID, " +
                "m.MPA_NAME as MPA_NAME from FILMS left join MPA m on FILMS.MPA_ID = m.MPA_ID";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm);

        log.info("Поступил запрос в базу на получение фильмов findAll():\n{}", films);

        return films;
    }

    @Override
    public Film save(Film film) {
        String sqlQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, " +
                "MPA_ID) values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        log.info("Фильму присвоен id={}", keyHolder.getKey().longValue());

        saveGenres(film);

        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update FILMS set FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, " +
                "DURATION = ?, MPA_ID = ? where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        saveGenres(film);
        return film;
    }

    private void saveGenres(Film film) {
        String sqlQueryDelGenres = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDelGenres, film.getId());
        String sqlQueryForGenre = "insert into FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
        if (film.getGenres() != null) {
            for (Genre genre: film.getGenres()) {
                jdbcTemplate.update(sqlQueryForGenre, film.getId(), genre.getId());
                log.info("saveGenre() - Фильм id=" + film.getId() + ". Жанр id=" + genre.getId());
            }
        }
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Film findFilmById(long id) {
        final String sqlQuery = "select FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, " +
                "m.MPA_ID as MPA_ID, m.MPA_NAME as MPA_NAME from FILMS left join MPA as m" +
                " on FILMS.MPA_ID = m.MPA_ID where FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm, id);
        if (films.size() == 0) {
            return null;
        }
        Film film = films.get(0);
        log.info("Жанры {}", film.getGenres());
        return film;
    }

    private LinkedHashSet<Genre> getGenreByFilmId(long id) {
        String sqlQuery = "select GENRE_ID, GENRE_NAME from GENRES " +
                "where GENRE_ID in (select GENRE_ID from FILM_GENRE where FILM_ID = ?);";
        return new LinkedHashSet<>(jdbcTemplate.query(sqlQuery, genreStorage::makeGenre, id));
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sqlQuery = "insert into LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userid) {
        String sqlQuery = "delete from LIKES where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userid);
    }

    @Override
    public List<Film> getPopularFilm(long count) {
        log.warn("Запрос getPopularFilm(long count), count={}", count);
        String sqlQuery = "select f.FILM_ID as FILM_ID, f.FILM_NAME as FILM_NAME, f.DESCRIPTION as DESCRIPTION, " +
                "f.RELEASE_DATE as RELEASE_DATE, f.DURATION as DURATION, " +
                "m.MPA_ID as MPA_ID, m.MPA_NAME as MPA_NAME from FILMS as f " +
                "left join FILM_GENRE as fg on f.FILM_ID = fg.FILM_ID " +
                "left join MPA as m on f.MPA_ID = m.MPA_ID " +
                "left join LIKES l on f.FILM_ID = l.FILM_ID " +
                "group by f.FILM_ID " +
                "order by count(l.USER_ID) desc " +
                "limit ?";
        return jdbcTemplate.query(sqlQuery, this::makeFilm, count);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("FILM_ID"))
                .name(rs.getString("FILM_NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .genres(getGenreByFilmId(rs.getLong("FILM_ID")))
                .mpa(new Mpa(rs.getLong("MPA_ID"), rs.getString("MPA_NAME")))
                .build();
    }
}
