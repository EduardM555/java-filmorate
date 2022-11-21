package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User extends StorageData {

//    private Long id;
    @Email
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    @JsonIgnore
    private Set<Long> friendsIds = new HashSet<>();

    public User(@Valid String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
