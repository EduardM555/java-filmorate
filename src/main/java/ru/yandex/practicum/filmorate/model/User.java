package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
public class User {

    private int id;
    @Email
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public User(@Valid String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
