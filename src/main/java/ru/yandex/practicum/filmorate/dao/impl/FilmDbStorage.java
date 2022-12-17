package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    @Override
//    public Map<Long, Film> findAll() {
//        return null;
//    }

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
        String sqlQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION," +
                " MPA_ID) values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        log.info("Фильму присвоен id={}", keyHolder.getKey().longValue());
        if (film.getGenres() != null) {
            List<Genre> genres = film.getGenres().stream()
                    .collect(Collectors.toList());
            String sqlQueryGenres = "insert into FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
            for (Genre genre: genres) {
                jdbcTemplate.update(sqlQueryGenres, film.getId(), genre.getId());
            }
//        } else {
//            String sqlQueryGenres = "delete from FILM_GENRE where FILM_ID = ?";
//            jdbcTemplate.update(sqlQueryGenres, film.getId());
        }
        return film;
//            final LocalDate birthday = user.getBirthday();
//            if (birthday == null) {
//                stmt.setNull(4, Types.DATE);
//            } else {
//                stmt.setDate(4, Date.valueOf(birthday));
//            }
    }

    Film makeStr(Film film, ResultSet rs) throws SQLException {
        String name = rs.getString("MPA_NAME");
        film.getMpa().setName(name);
        return film;

    }

    private void addFilmGenre(Film film) {
        if (film.getGenres() != null) {
            String sqlQuery = "insert into FILM_GENRE (FILM_ID, GENRE_ID) " +
                    "VALUES (?, ?)";
            for (Genre genre: film.getGenres())
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
    }

    private String getMpaNameToFilm(Film film) {
        if (film.getMpa() != null) {
            String sqlQuery = "select MPA_NAME from MPA, FILMS where MPA_ID = FILMS.MPA_ID and MPA.MPA_ID = ?";
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, film.getMpa().getId());
            String mpaName;
            if (sqlRowSet.next()) {
                mpaName = sqlRowSet.getString("MPA_NAME");
                System.out.println(mpaName);
                return mpaName;
            }
        }
        return null;
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
        return film;
    }

    @Override
    public void delete(long id) {
    }

    @Override
    public Film findFilmById(long id) {
        final String sqlQuery = "select FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, " +
                "m.MPA_ID as MPA_ID , m.MPA_NAME as MPA_NAME from FILMS left join MPA as m" +
                " on FILMS.MPA_ID = m.MPA_ID where FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, id);
        System.out.println("Список фильмов:\n" + films);
        if (films.size() == 0) {
            return null;
        }
        return films.get(0);
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
                new Mpa(rs.getLong("MPA_ID"), rs.getString("MPA_NAME")));
    }
}
