package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre getGenreById(long id) {
        Genre genre = genreStorage.getGenreById(id);
        if (genre == null) {
            log.warn("Жанр равен null={}", genre);
            throw new GenreNotFoundException("Жанра в базе нет с id=" + id);
        }
        log.info("Жанр успешно получен и будет возвращен. {}", genre);
        return genre;
    }
}
