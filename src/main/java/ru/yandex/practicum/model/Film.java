package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NonNull
public class Film {

    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @Builder.Default
    private Set<Long> likesId = new HashSet<>(); //id юзеров, которые оставили лайк

}
