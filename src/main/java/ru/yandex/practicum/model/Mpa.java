package ru.yandex.practicum.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Mpa {
    private long id;
    private String name;

    public Mpa(long id) {
        this.id = id;
    }
}
