package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface GenreStorage {
    List<Genre> findAll();
    Genre getGenreById(long id);
    Genre makeGenre(ResultSet rs, int rowNum) throws SQLException;
}
