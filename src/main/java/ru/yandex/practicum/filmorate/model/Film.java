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
    private Mpa mpa;
//    @JsonIgnore
//    private Set<Long> userIds = new HashSet<>();
//    @JsonIgnore
//    private long rate = 0;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

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
