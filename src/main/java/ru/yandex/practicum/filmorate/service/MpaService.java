package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    public Mpa getMpaById(long id) {
        log.warn("Запрос на получения MPA с id={}", id);
        Mpa mpa = mpaStorage.getMpaById(id);
        log.warn("MPA должен быть получен = {}", mpa);
        if (mpa == null) {
            log.warn("MPA равен null={}", mpa);
            throw new MpaNotFoundException("В базе нет рейтинга MPA с id=" + id);
        }
        log.warn("MPA успешно получен и будет возвращен. {}", mpa);
        return mpa;
    }
}
