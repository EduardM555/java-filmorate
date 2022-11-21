package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.StorageData;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface Storage<T extends StorageData> {
    Map<Long, T> getAll();
    T create(T data);
    T update(T data);
    T delete(long id);
    T getById(long id);
}
