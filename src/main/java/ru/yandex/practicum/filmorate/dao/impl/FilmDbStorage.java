package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.List;

@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "select FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, m.MPA_ID as MPA_ID, " +
                "m.MPA_NAME as MPA_NAME from FILMS left join MPA m on FILMS.MPA_ID = m.MPA_ID";
        List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
        log.info("Поступил запрос в базу на получение фильмов findAll():\n{}", films);
//        return jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
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

        String sqlQueryGenres = "insert into FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
        saveGenres(film, sqlQueryGenres);

        return film;
    }

//    Film makeStr(Film film, ResultSet rs) throws SQLException {
//        String name = rs.getString("MPA_NAME");
//        film.getMpa().setName(name);
//        return film;
//
//    }

//    private void addFilmGenre(Film film) {
//        if (film.getGenres() != null) {
//            String sqlQuery = "insert into FILM_GENRE (FILM_ID, GENRE_ID) " +
//                    "VALUES (?, ?)";
//            for (Genre genre: film.getGenres())
//            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
//        }
//    }

//    private String getMpaNameToFilm(Film film) {
//        if (film.getMpa() != null) {
//            String sqlQuery = "select MPA_NAME from MPA, FILMS where MPA_ID = FILMS.MPA_ID and MPA.MPA_ID = ?";
//            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, film.getMpa().getId());
//            String mpaName;
//            if (sqlRowSet.next()) {
//                mpaName = sqlRowSet.getString("MPA_NAME");
//                System.out.println(mpaName);
//                return mpaName;
//            }
//        }
//        return null;
//    }

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

        String sqlQueryForGenre = "update FILM_GENRE set FILM_ID = ?, GENRE_ID = ?";
        saveGenres(film, sqlQueryForGenre);
        return film;
    }

    private void saveGenres(Film film, String sqlQueryForGenre) {
        String sqlQueryGenres = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sqlQueryGenres, film.getId());
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            for (Genre genre: film.getGenres()) {
                log.info("Фильм id=" + film.getId() + ". Жанр id=" + genre.getId());
                jdbcTemplate.update(sqlQueryForGenre, film.getId(), genre.getId());
            }
        } else {
            log.warn("БЕЗ ЖАНРА size={}", film.getGenres().size());
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
                "m.MPA_ID as MPA_ID , m.MPA_NAME as MPA_NAME from FILMS left join MPA as m" +
                " on FILMS.MPA_ID = m.MPA_ID where FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, id);
        log.info("Список фильмов:\n" + films);
        if (films.size() == 0) {
            return null;
        }
        Film film = films.get(0);

        return film;
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

    public List<Film> getPopularFilm(long count) {
        return null;
    }

    static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
//        long rs1 = rs.getLong("FILM_ID");
//        System.out.println(rs1);
//        String rs2 = rs.getString("FILM_NAME");
//        System.out.println(rs2);
//        String rs3 =  rs.getString("DESCRIPTION");
//        System.out.println(rs3);
//        LocalDate rs4 = rs.getDate("RELEASE_DATE").toLocalDate();
//        System.out.println(rs4);
//        int rs5 = rs.getInt("DURATION");
//        System.out.println(rs5);
//        long rs6 = rs.getLong("MPA_ID");
//        System.out.println(rs6);
//        String rs7 = rs.getString("MPA_NAME");
//        System.out.println(rs7);
        return new Film(rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                new LinkedHashSet<>(),
                new Mpa(rs.getLong("MPA_ID"), rs.getString("MPA_NAME")));
    }
}
