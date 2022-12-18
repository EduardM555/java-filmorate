package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    List<Film> findAll();
    Film save(Film film);
    Film update(Film film);
    void delete(long id);
    Film findFilmById(long id);
    void addLike(long filmId, long userId);
    void removeLike(long filmId, long userid);
    List<Film> getPopularFilm(long count);
}
