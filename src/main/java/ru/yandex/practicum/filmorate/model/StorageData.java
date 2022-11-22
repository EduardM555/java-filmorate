package ru.yandex.practicum.filmorate.model;

public abstract class StorageData {
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
