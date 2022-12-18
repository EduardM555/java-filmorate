package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Mpa {
    private long id;
    private String name;

    public Mpa(Long id) {
        this.id = id;
    }
}
