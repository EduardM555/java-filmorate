package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Genre {
    private long id;
    private String name;

    public Genre(long id) {
        this.id = id;
    }

}
