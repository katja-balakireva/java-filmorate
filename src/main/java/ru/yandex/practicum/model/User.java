package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
@NonNull
public class User {

    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
