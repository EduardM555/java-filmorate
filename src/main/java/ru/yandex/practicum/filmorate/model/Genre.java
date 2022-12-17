package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Genre {
    private long id;
    private String name;

    public Genre(long id) {
        this.id = id;
    }

    public Genre(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
