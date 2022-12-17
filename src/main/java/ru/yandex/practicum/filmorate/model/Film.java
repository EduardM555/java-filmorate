package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = "id")
public class Film {
    private long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
//    private Integer rate;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    private Mpa mpa;

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

//    public Film(String name, String description, LocalDate releaseDate, int duration) {
//        this.name = name;
//        this.description = description;
//        this.releaseDate = releaseDate;
//        this.duration = duration;
//    }

//    public void addLike(long userId) {
//        userIds.add(userId);
//        rate = userIds.size();
//    }
//
//    public void removeLike(long userId) {
//        if (!userIds.contains(userId)) {
//            throw new UserNotFoundException("Пользователя id: " + userId + " в базе нет.");
//        }
//        userIds.remove(userId);
//        rate = userIds.size();
//    }
}
