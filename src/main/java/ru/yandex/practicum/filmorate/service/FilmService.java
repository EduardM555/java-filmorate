package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private long generatedId = 0;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.getAll().values();
    }

    public Film create(Film film) {
        if (!validate(film)) {
            log.info("Ошибка создания объекта Film: {}", film);
            throw new ValidationException("Ошибка валидации фильма при создании.");
        }
        film.setId(generateId());
        log.info("Объекту класса Film присовен id: {}", film.getId());
        return filmStorage.create(film);
    }

    public Film putFilm(@Valid @RequestBody Film film) {
        if (!validate(film)) {
            log.info("Ошибка обновления объекта Film: {}", film);
            throw new ValidationException("Ошибка валидации фильма при обновлении.");
        }
        return filmStorage.update(film);
    }

    public Film getFilmById(long filmId) {
        Film film = filmStorage.getById(filmId);
        if (!validate(film)) {
            log.info("Ошибка получения объекта Film: {}", film);
            throw new ValidationException("Ошибка валидации фильма при получении.");
        }
        return film;
    }

    public Film delete(long id) {
        return filmStorage.delete(id);
    }


    public void addLike(long id, long userId) {
        filmStorage.getById(id).addLike(userId);
    }

    public void removeLike(long id, long userid) {
        filmStorage.getById(id).removeLike(userid);
    }

    public List<Film> getPopularFilms(long count) {
        return findAll().stream()
                .sorted((x1, x2) -> (int) (x2.getRate() - x1.getRate()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private boolean validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) return false;
        if (film.getDescription().length() > 200) return false;
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) return false;
        if (film.getDuration() < 0) return false;
        return true;
    }

    private long generateId() {
        ++generatedId;
        return generatedId;
    }
}
