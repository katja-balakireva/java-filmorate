package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ServerErrorException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
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

    public void addFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user != null && friend != null) {
            user.setAndCheckFriendsId(friendId);
            friend.setAndCheckFriendsId(userId);

        } else throw new NotFoundException("Пользователь не найден");
    }

    public void removeFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user != null && friend != null) {
            Set<Long> usersFriends = user.getFriendsId();
            Set<Long> friendsFriends = friend.getFriendsId();
            usersFriends.remove(friendId);
            friendsFriends.remove(userId);
        } else throw new ServerErrorException("Ошибка сервера");
    }

    public List<User> getAllFriends(long userId) {
        User user = userStorage.getById(userId);
        List<User> friendsList = new ArrayList<>();
        Set<Long> userFriendsIdList = user.getFriendsId();

        if (userFriendsIdList != null) {
            for (Long id : user.getFriendsId()) {
                User friend = userStorage.getById(id);
                friendsList.add(friend);
            }
        }
        return friendsList;
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        Set<Long> userFriendsIdList = userStorage.getById(userId).getFriendsId();
        Set<Long> otherFriendsIdList = userStorage.getById(otherId).getFriendsId();
        List<User> commonFriendsList = new ArrayList<>();

        if (userFriendsIdList != null && otherFriendsIdList != null) {
            Set<Long> commonFriendsIdList = new HashSet<>(userFriendsIdList);
            commonFriendsIdList.retainAll(otherFriendsIdList);

            for (Long id : commonFriendsIdList) {
                User user = userStorage.getById(id);
                commonFriendsList.add(user);
            }
        }
        return commonFriendsList;
    }

    private boolean validateUser(User user) throws ValidationException {
        if (user.getName().isBlank() || user.getName().isEmpty()) {
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
