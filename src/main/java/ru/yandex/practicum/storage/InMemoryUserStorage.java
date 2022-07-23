package ru.yandex.practicum.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.User;

import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage {

    private static long idCounter = 0;
    private Set<User> users = new HashSet<>();


    @Override
    public User add(User userToAdd) {
        long newId = ++idCounter;
        userToAdd.setId(newId);
        users.add(userToAdd);
        return userToAdd;
    }

    @Override
    public User update(User userToUpdate) {
        for (User user : users) {
            if (user.getId() == userToUpdate.getId()) {
                users.remove(user);
                users.add(userToUpdate);
            }
        }
        return userToUpdate;
    }

    @Override
    public User remove(User userToRemove) {
        users.remove(userToRemove);
        return userToRemove;
    }

    @Override
    public Set<User> getAll() {
        return users;
    }

    @Override
    public User getById(long userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }
}