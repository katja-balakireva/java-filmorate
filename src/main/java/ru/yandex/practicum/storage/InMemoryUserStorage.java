package ru.yandex.practicum.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.controllers.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage{

    private static long idCounter = 0;
    private Set<User> users = new HashSet<>();


    @Override
    public User add(User user) {
        for (User u: users) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже существует");
            }
        }

        if (validateUser(user)) {
            long newId = ++idCounter;
            user.setId(newId);
            users.add(user);
          //  log.info("Добавлен пользователь: {} с id: {}", user.getLogin(), user.getId());
        }
        return user;
    }

    @Override
    public User update(User user) {

        if (validateUser(user)) {
            for (User u : users) {
                if (u.getId() == user.getId()) {
                    users.remove(u);
                    users.add(user);
                    break;
                     //проверить как удаляются
                   // log.info("Информация о пользователе {} обновлена.", user.getLogin());
                }
            }
        }
        return user;
        }

    @Override
    public User remove(User user) {

        if (validateUser(user)) {
                if (users.contains(user)) {
                    users.remove(user);
                    // log.info("Пользователь с логином {} удалён.", user.getLogin());
                }
            }
        return user; //проверить не будет ли ошибки
    }

    @Override
    public Set<User> getAll() {
        return users;
    }

    @Override
    public User getById(long userId) {
        for (User u: users) {
            if (u.getId() == userId) {
                return u;
            }
        }
        return null;
    }

    private boolean validateUser(User user) throws ValidationException {

        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail().isBlank() || user.getEmail().isEmpty()) {
           // log.warn("Ошибка валидации email пользователя");
            throw new ValidationException("Пустой email пользователя");
        }
        if (!user.getEmail().contains("@")) {
         //   log.warn("Ошибка валидации email пользователя");
            throw new ValidationException("email пользователя не содержит @");
        }
        if (user.getLogin().isBlank() || user.getLogin().isEmpty()) {
          //  log.warn("Ошибка валидации логина пользователя");
            throw new ValidationException("Пустой логин пользователя");
        }
        if (user.getLogin().contains(" ")) {
         //   log.warn("Ошибка валидации логина пользователя");
            throw new ValidationException("Логин пользователя содержит пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
          //  log.warn("Ошибка валидации даты рождения пользователя");
            throw new ValidationException("Дата рождения пользователя в будущем");
        }
        return true;
    }
}
