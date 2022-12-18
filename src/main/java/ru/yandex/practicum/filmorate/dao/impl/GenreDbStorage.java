package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        String sqlQuery = "select GENRE_ID, GENRE_NAME from GENRES";
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    @Override
    public Genre getGenreById(long id) {
        log.warn("Делается запрос в БАЗУ на получение жанра Genre по id={}", id);
        String sqlQuery = "select GENRE_ID, GENRE_NAME from GENRES where GENRE_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::makeGenre, id);
        if (genres == null || genres.isEmpty())  {
            return null;
        }
        log.warn("Жанры genres получены: {}", genres);
        return genres.get(0);
    }

        public Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getLong("GENRE_ID"),
                rs.getString("GENRE_NAME"));
    }
}
