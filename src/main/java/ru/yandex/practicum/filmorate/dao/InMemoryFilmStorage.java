//package ru.yandex.practicum.filmorate.dao;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Component
//public class InMemoryFilmStorage implements FilmStorage {
//
//    private final Map<Long, Film> films = new HashMap<>();
//
//    @Override
//    public Map<Long, Film> findAll() {
//        return films;
//    }
//
//    @Override
//    public Film save(Film film) {
//        films.put(film.getId(), film);
//        log.info("Успешное создание объекта: {}", film);
//        return film;
//    }
//
//    @Override
//    public Film update(Film film) {
//        if (!films.containsKey(film.getId())) {
//            throw new FilmNotFoundException("Фильма: " + film + " в базе нет.");
//        }
//        films.put(film.getId(), film);
//        log.info("Успешное обновление объекта Film: {}", film);
//        return film;
//    }
//
//    @Override
//    public void delete(long id) {
//        if (!films.containsKey(id)) {
//            throw new FilmNotFoundException("Фильма с id: " + id + " в базе нет.");
//        }
//        Film deletedFilm = films.get(id);
//        films.remove(id);
//        log.info("Успешное удаление объекта Film с id: {}", id);
//        return deletedFilm;
//    }
//
//    @Override
//    public Film findFilmById(long id) {
//        if (!films.containsKey(id)) {
//            throw new FilmNotFoundException("Фильма с id: " + id + " в базе нет.");
//        }
//        log.info("Следующее действие получение объекта Film по id: {}", id);
//        return films.get(id);
//    }
//}
