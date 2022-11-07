package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private Map<Integer, Film> films = new HashMap();
    private int generatedId = 0;

    public Map<Integer, Film> getFilms() {
        return films;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (!validate(film)) {
            log.info("Ошибка создания объекта Film: {}", film);
            throw new ValidationException("Ошибка валидации фильма при создании.");
        }
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Успешное создание объекта: {}", film);
        return film;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильма: " + film + " в базе нет.");
        }
        if (!validate(film)) {
            log.info("Ошибка обновления объекта Film: {}", film);
            throw new ValidationException("Ошибка валидации фильма при обновлении.");
        }
        films.put(film.getId(), film);
        log.info("Успешное обновление объекта Film: {}", film);
        return film;
    }

    private boolean validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) return false;
        if (film.getDescription().length() > 200) return false;
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) return false;
        if (film.getDuration() < 0) return false;
        return true;
    }

    private int generateId() {
        ++generatedId;
        log.info("Объекту класса Film присовен id: {}", generatedId);
        return generatedId;
    }

}
