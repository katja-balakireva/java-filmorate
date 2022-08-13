package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Set<User> getAllUsers() {
        Set<User> userList = userService.getAllUsers();
        log.info("Получен список из {} пользователей", userList.size());
        return userList;
    }

    @GetMapping(value = "{userId}")
    public User getById(@PathVariable long userId) {
        User user = userService.getUserById(userId);
        log.info("Получен пользователь {} с id {}", user.getLogin(), user.getId());
        return user;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        User addedUser = userService.addUser(user);
        log.info("Добавлен пользователь: {} с id: {}", addedUser.getLogin(), addedUser.getId());
        return addedUser;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        log.info("Информация о пользователе {} обновлена.", updatedUser.getLogin());
        return updatedUser;
    }

    @DeleteMapping
    public void deleteUser(@RequestBody User user) {
        log.info("Пользователь {} с id {} удалён", user.getLogin(), user.getId());
        userService.removeUser(user);
    }

    @PutMapping(value = "{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователь c id {} добавил друга c id {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователь c id {} удалил друга c id {}", id, friendId);
        userService.removeFriend(id, friendId);
    }

    @GetMapping(value = "{id}/friends")
    public List<User> getFriendsList(@PathVariable long id) {
        List<User> friendsList = userService.getAllFriends(id);
        log.info("Получен список друзей пользователя c id {}, {} друзей", id, friendsList.size());
        return friendsList;
    }

    @GetMapping(value = "{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        List<User> commonFriendsList = userService.getCommonFriends(id, otherId);
        log.info("Получены общие друзья пользователей {} и {}: {}", id, otherId, commonFriendsList);
        return commonFriendsList;
    }
}
