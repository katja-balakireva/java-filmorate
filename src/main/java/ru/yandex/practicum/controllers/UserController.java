package ru.yandex.practicum.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.HashSet;

@RestController
@RequestMapping("/users")
public class UserController {
    private HashSet<User> users = new HashSet<>();

    @GetMapping
    public HashSet getAllUsers() {
        return users;
    }

    @PutMapping
    //"/users?id=1"
    public User updateUser(@RequestParam("id") int id, @RequestBody User user) throws ValidationException {

        if (validateUser(user)) {
            for (User u: users) {
                if (u.getId() == id) {
                    users.add(user);
                }
            }
        }
        return user;
    }

    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {

        if (validateUser(user)) {
            users.add(user);
        }
        return user;
    }

    private boolean validateUser(User user) throws ValidationException {

        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        } else {
            if (user.getEmail().isBlank() || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
                throw new ValidationException("Ошибка валидации email пользователя");
            }
            if (user.getLogin().isBlank() || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
                throw new ValidationException("Ошибка валидации логина пользователя");
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Ошибка валидации даты рождения пользователя");
            }
        }
        return true;
    }
}
