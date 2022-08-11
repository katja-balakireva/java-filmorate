package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ServerErrorException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.FriendStorage;
import ru.yandex.practicum.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    @Qualifier("UserDbStorage")
    private UserStorage userStorage;
    @Qualifier("FriendDbStorage")
    private FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage,  @Qualifier("FriendDbStorage") FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public Set<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(long userId) {

        if (userStorage.getById(userId) != null) {
            return userStorage.getById(userId);
        } else throw new NotFoundException("Пользователь с таким id не найден");
    }

    public User addUser(User userToAdd) {
        for (User user : userStorage.getAll()) {
            if (user.getEmail().equals(userToAdd.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже существует");
            }
        }
        if (validateUser(userToAdd)) {
            return userStorage.add(userToAdd);
        }
        return userToAdd;
    }

    public User updateUser(User userToUpdate) {
        if (validateUser(userToUpdate)) {
            for (User user : userStorage.getAll()) {
                if (user.getId() == userToUpdate.getId()) {
                    return userStorage.update(userToUpdate);
                }
            }
            throw new NotFoundException("Пользователь с таким id не найден");
        } else {
            throw new ValidationException("Невозможно обновить информацию о пользователе");
        }
    }

    public void removeUser(User userToRemove) {
        if (validateUser(userToRemove)) {
            if (userStorage.getAll().contains(userToRemove)) {
                userStorage.getAll().remove(userToRemove);
            } else {
                throw new NotFoundException("Такого пользователя нет в списке пользователей");
            }
        }
    }

    //friends

    public void addFriend(long userId, long friendId) {
        friendStorage.addFriend(userId,friendId);
    }

    public void removeFriend(long userId, long friendId) {
        friendStorage.removeFriend(userId, friendId);
    }

    public List<User> getAllFriends(long userId) {
        return friendStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(long userId, long otherId) {
       return friendStorage.getCommonFriends(userId,otherId);
    }

    private boolean validateUser(User user) throws ValidationException {
        if (user.getName().isBlank() || user.getName().isEmpty() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getEmail().isBlank() || user.getEmail().isEmpty()) {
            log.warn("Ошибка валидации email пользователя: {}", user);
            throw new ValidationException("Пустой email пользователя");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Ошибка валидации email пользователя: {}", user);
            throw new ValidationException("email пользователя не содержит @");
        }
        if (user.getLogin().isBlank() || user.getLogin().isEmpty()) {
            log.warn("Ошибка валидации логина пользователя: {}", user);
            throw new ValidationException("Пустой логин пользователя");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Ошибка валидации логина пользователя: {}", user);
            throw new ValidationException("Логин пользователя содержит пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка валидации даты рождения пользователя: {}", user);
            throw new ValidationException("Дата рождения пользователя в будущем");
        }
        return true;
    }
}
