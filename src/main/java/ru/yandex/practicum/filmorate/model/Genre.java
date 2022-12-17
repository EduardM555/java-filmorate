package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Genre {
    private long id;
    private String genreName;

    public Genre(long id) {
        this.id = id;
    }
}
