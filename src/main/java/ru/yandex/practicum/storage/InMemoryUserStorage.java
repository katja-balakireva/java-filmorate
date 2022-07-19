package ru.yandex.practicum.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
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
                    return user;
                }
            }
            throw new NotFoundException("Пользователь с таким id не найден");
        } else {
            throw new ValidationException("Невозможно обновить информацию о пользователе");
        }
    }

    @Override
    public User remove(User user) {

        if (validateUser(user)) {
                if (users.contains(user)) {
                    users.remove(user);
                } else {
                    throw new NotFoundException("Такого пользователя нет в списке пользователей");
                }
            }
        return user;
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
        throw new NotFoundException("Пользователь с таким id не найден");
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
