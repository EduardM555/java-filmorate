package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<Mpa> findAll() {
        String sqlQuery = "select MPA_ID, MPA_NAME from MPA";
        return jdbcTemplate.query(sqlQuery, MpaDbStorage::makeMpa);
    }

    public Mpa getMpaById(long id) {
        String sqlQuery = "select MPA_ID, MPA_NAME from MPA where MPA_ID = ?";
        return jdbcTemplate.query(sqlQuery, MpaDbStorage::makeMpa, id).get(0);
    }

    private static Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getLong("MPA_ID"),
                rs.getString("MPA_NAME"));
    }
}
