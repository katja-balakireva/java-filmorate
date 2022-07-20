package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {

    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> likesId;

    public void setAndCheckLikes(long userId) {
        if (this.likesId == null) {
            this.likesId = new HashSet<>();
            this.likesId.add(userId);
        } else {
            this.likesId.add(userId);
        }
    }
}
