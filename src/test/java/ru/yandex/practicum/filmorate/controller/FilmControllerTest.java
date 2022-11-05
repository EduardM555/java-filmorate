package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    FilmController filmController;
    Film film1 = new Film("film1Name", "film1Description",
            LocalDate.of(2000, 1, 1), 90);
    Film film2 = new Film("film2Name", "film2Description",
            LocalDate.of(2001, 1, 1), 90);

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void findAll() {
        assertEquals(0, filmController.getFilms().size());
        filmController.getFilms().put(1, film1);
        filmController.getFilms().put(2, film2);
        assertEquals(2, filmController.getFilms().size());
    }

    @Test
    void create() {
        filmController.create(film1);

        assertEquals(film1, filmController.getFilms().get(1));
    }

    @Test
    void putFilm() {
        filmController.putFilm(film1);

        assertEquals(film1, filmController.getFilms().get(1));
    }

    @Test
    void createFilmWithBlankName() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.create(new Film(" ", "film3Description",
                        LocalDate.of(2002, 1, 1), 90)));

        assertEquals("Ошибка валидации фильма.", exception.getMessage());
    }

    @Test
    void createFilmWithDescriptionLength200() {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < 200; i++) {
            stringBuilder.append(1);
        }
        String s = stringBuilder.toString();
        Film film = new Film("film3Name", s,
                LocalDate.of(2002, 1, 1), 90);
        filmController.create(film);

        assertEquals(200, filmController.getFilms().get(3).getDescription().length());
    }

    @Test
    void createFilmWithDescriptionLength201() {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < 201; i++) {
            stringBuilder.append(1);
        }
        String s = stringBuilder.toString();
        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.create(new Film("film3Name", s,
                        LocalDate.of(2002, 1, 1), 90)));

        assertEquals("Ошибка валидации фильма.", exception.getMessage());
    }
}