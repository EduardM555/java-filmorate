package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Mpa {
    private Long id;
    private String mpaName;

    public Mpa(Long id) {
        this.id = id;
    }
}
