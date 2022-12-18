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
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre);
    }

    @Override
    public Genre getGenreById(long id) {
        String sqlQuery = "";
        return jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, id).get(0);
    }

    private static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getLong("GENRE_ID"),
                rs.getString("GENRE_NAME"));
    }
}
