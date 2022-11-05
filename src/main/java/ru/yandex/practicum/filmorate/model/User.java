package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
public class User {

    private int id = 0;
    @Email private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.id = generateId();
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    private int generateId() {
        this.id++;
        return id;
    }

}
