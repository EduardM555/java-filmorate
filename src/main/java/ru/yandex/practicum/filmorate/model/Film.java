package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Film extends StorageData {

    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    private Mpa mpa;
    @NonNull
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
    @JsonIgnore
    private Set<Long> userIds = new HashSet<>();
    @JsonIgnore
    private long rate = 0;
    private Set<Genre> genres;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addLike(long userId) {
        userIds.add(userId);
        rate = userIds.size();
    }

    public void removeLike(long userId) {
        if (!userIds.contains(userId)) {
            throw new UserNotFoundException("Пользователя id: " + userId + " в базе нет.");
        }
        userIds.remove(userId);
        rate = userIds.size();
    }
}
