package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

//@Data
@Getter
@Setter
public class User extends StorageData {

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

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", friendsIds=" + friendsIds +
                ", id=" + id +
                '}';
    }
}
