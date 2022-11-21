package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film extends StorageData {

    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @JsonIgnore
    private Set<Long> userIds = new HashSet<>();
    @JsonIgnore
    private long rate = 0;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addLike(long userId) {
//        if (!userIds.contains(userId)) {
//            throw new UserNotFoundException("Пользователя id: " + userId + " в базе нет.");
//        }
        userIds.add(userId);
        rate = userIds.size();
    }

    public void removeLike(long userId) {
//        if (!userIds.contains(userId)) {
//            throw new UserNotFoundException("Пользователя id: " + userId + " в базе нет.");
//        }
        userIds.remove(userId);
        rate = userIds.size();
    }
}
