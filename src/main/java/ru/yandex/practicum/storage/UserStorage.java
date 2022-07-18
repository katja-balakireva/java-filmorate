package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.User;

import java.util.Set;

public interface UserStorage {

    User add(User user);
    User update(User user);
    User remove(User user);
    Set<User> getAll();
    User getById(long userId);
}
