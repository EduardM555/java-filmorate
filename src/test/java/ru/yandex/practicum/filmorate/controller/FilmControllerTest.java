package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmController filmController;
    private FilmService filmService;
    private Film film1 = new Film("film1Name", "film1Description",
            LocalDate.of(2000, 1, 1), 90);
    private Film film2 = new Film("film2Name", "film2Description",
            LocalDate.of(2001, 1, 1), 90);
    private Film film3 = new Film("film3Name", "film3Description",
            LocalDate.of(2002, 1, 1), 90);

    @BeforeEach
    void setUp() {
        filmController = new FilmController(filmService);
    }

    @Test
    void findAllAndCreate() {
        assertEquals(0, filmController.findAll().size());

        filmController.create(film1);
        filmController.create(film2);

        assertEquals(film1, filmController.getFilmById(film1.getId()));
        assertEquals(2, filmController.findAll().size());
    }

    @Test
    void putFilm() {
        filmController.create(film3);
        film3.setName("film3NewName");
        filmController.putFilm(film3);

        assertEquals(film3, filmController.getFilmById(film3.getId()));
    }

    @Test
    void createFilmWithBlankName() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.create(new Film(" ", "film4Description",
                        LocalDate.of(2002, 1, 1), 90)));

        assertEquals("Ошибка валидации фильма при создании.", exception.getMessage());
    }

    @Test
    void createFilmWithDescriptionLength200() {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < 200; i++) {
            stringBuilder.append(1);
        }
        String s = String.valueOf(stringBuilder);
        Film film = new Film("film5Name", s,
                LocalDate.of(2002, 1, 1), 90);
        filmController.create(film);

        assertEquals(200, filmController.getFilmById(film.getId()).getDescription().length());
    }

    @Test
    void createFilmWithDescriptionLength201() {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < 201; i++) {
            stringBuilder.append(1);
        }
        String s = String.valueOf(stringBuilder);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.create(new Film("film6Name", s,
                        LocalDate.of(2002, 1, 1), 90)));

        assertEquals("Ошибка валидации фильма при создании.", exception.getMessage());
    }
}