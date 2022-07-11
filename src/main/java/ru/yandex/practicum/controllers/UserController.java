package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.HashSet;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private HashSet<User> users = new HashSet<>();
    private static int idCounter = 0;

    @GetMapping
    public HashSet<User> getAllUsers() {
        return users;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {

        if (validateUser(user)) {
            for (User u : users) {
                if (u.getId() == user.getId()) {
                    users.remove(u);
                    users.add(user);
                    log.info("Информация о пользователе {} обновлена.", user.getLogin());
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return user;
    }

    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {

        if (validateUser(user)) {
            user.setId(++idCounter);
            users.add(user);
            log.info("Добавлен пользователь: {} с id: {}", user.getLogin(), user.getId());
        }
        return user;
    }

    private boolean validateUser(User user) throws ValidationException {

        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getEmail().isBlank() || user.getEmail().isEmpty()) {
            log.warn("Ошибка валидации email пользователя");
            throw new ValidationException("Пустой email пользователя");
        }

        if (!user.getEmail().contains("@")) {
            log.warn("Ошибка валидации email пользователя");
            throw new ValidationException("email пользователя не содержит @");
        }

        if (user.getLogin().isBlank() || user.getLogin().isEmpty()) {
            log.warn("Ошибка валидации логина пользователя");
            throw new ValidationException("Пустой логин пользователя");
        }

        if (user.getLogin().contains(" ")) {
            log.warn("Ошибка валидации логина пользователя");
            throw new ValidationException("Логин пользователя содержит пробелы");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка валидации даты рождения пользователя");
            throw new ValidationException("Дата рождения пользователя в будущем");
        }

        return true;

    }
}
