package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmStorage filmStorage;
    private Film film;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setName("Name");
        film.setDescription("Descr");
        film.setReleaseDate(LocalDate.of(2010, 12, 1));
        film.setDuration(90);
        film.setMpa(new Mpa(1, null));
        film.setGenres(new LinkedHashSet<>());
        filmStorage.save(film);
    }

    @Test
    void findAll() {
        List<Film> newFilms = filmStorage.findAll();

        assertNotNull(newFilms);
        assertEquals(film, newFilms.get(0));
    }

    @Test
    void update() {
        Film newFilm = filmStorage.findAll().get(1);
        LinkedHashSet<Genre> genres = new LinkedHashSet<>(Set.of(new Genre(1, null)));
        newFilm.setName("New Film Name");
        newFilm.setDescription("New Film Descr");
        newFilm.setGenres(genres);
        filmStorage.update(newFilm);
        Film newFilm2 = filmStorage.findFilmById(2);

        assertEquals("New Film Name", newFilm.getName());
        assertEquals("New Film Descr", newFilm.getDescription());

        assertThat(newFilm2)
                .hasFieldOrPropertyWithValue("genres", newFilm.getGenres())
                .hasFieldOrPropertyWithValue("name", newFilm.getName())
                .hasFieldOrPropertyWithValue("description", newFilm.getDescription());
    }

    @Test
    void findFilmById() {
        Film byId = filmStorage.findFilmById(film.getId());

        assertNotNull(byId);
        assertEquals(film.getId(), byId.getId());
        assertEquals(film.getName(), byId.getName());

        assertThat(byId)
                .hasFieldOrPropertyWithValue("id", film.getId())
                .hasFieldOrPropertyWithValue("duration", film.getDuration());
    }
}