package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private Map<Integer, Film> films = new HashMap();
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

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
            log.info("Ошибка", new ValidationException("Ошибка валидации фильма."));
            throw new ValidationException("Ошибка валидации фильма.");
        }
        films.put(film.getId(), film);
        log.info("Объект: {}");
        return film;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        return create(film);
    }

    boolean validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) return false;
        if (film.getDescription().length() > 200) return false;
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) return false;
        if (film.getDuration() < 0) return false;
        return true;
    }

}
