package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public interface UserStorage {

    User add(User user);

    User update(User user);

    User remove(User user);

    void removeAll();

    Set<User> getAll();

    User getById(long userId);

    User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException;
}
