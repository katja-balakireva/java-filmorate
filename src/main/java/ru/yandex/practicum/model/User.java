package ru.yandex.practicum.model;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class User {

    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
