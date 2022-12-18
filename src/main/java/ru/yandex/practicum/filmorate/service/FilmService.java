package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmDbStorage;
    private final UserService userService;


    public List<Film> findAll() {
        log.info("Сейчас будет запрос в базу на получение фильмов findAll()");
        return filmDbStorage.findAll();
    }

    public Film create(Film film) {
        if (!validate(film)) {
            log.info("Ошибка создания объекта Film: {}", film);
            throw new ValidationException("Ошибка валидации фильма при создании.");
        }
        log.info("Сейчас будет запись фильма в базу и присоение id");
        return filmDbStorage.save(film);
    }

    public Film putFilm(Film film) {
        getFilmById(film.getId());
        if (!validate(film)) {
            log.info("Ошибка обновления объекта Film: {}", film);
            throw new ValidationException("Ошибка валидации фильма при обновлении.");
        }
        return filmDbStorage.update(film);
    }

    public Film getFilmById(long filmId) {
        Film film = filmDbStorage.findFilmById(filmId);
        if (film == null) {
            throw new FilmNotFoundException("Фильма с id=" + filmId + " в базе нет.");
        }
        return film;
    }

    public void delete(long id) {
        filmDbStorage.delete(id);
    }


    public void addLike(long id, long userId) {
        userService.getUserById(userId);
        getFilmById(id);
        filmDbStorage.addLike(id, userId);
    }

    public void removeLike(long id, long userid) {
        userService.getUserById(userid);
        getFilmById(id);
        filmDbStorage.removeLike(id, userid);
    }

    public List<Film> getPopularFilms(long count) {
//        return findAll().stream()
//                .sorted((x1, x2) -> (int) (x2.getRate() - x1.getRate()))
//                .limit(count)
//                .collect(Collectors.toList());
        return null;
    }

    private boolean validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) return false;
        if (film.getDescription().length() > 200) return false;
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) return false;
        if (film.getDuration() < 0) return false;
        return true;
    }
}
