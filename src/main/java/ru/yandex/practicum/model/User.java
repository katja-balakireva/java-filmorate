package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NonNull
public class User {

    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @Builder.Default
    private Set<Long> friendsId = new HashSet<>();

}
