package ru.yandex.practicum.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        long userId = simpleJdbcInsert.executeAndReturnKey(toMap(user)).longValue();
        user.setId(userId);
        return user;
    }

    @Override
    public User update(User user) {

        String sqlQuery = "update users set " +
                "NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ? " +
                "where ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getEmail()
                , user.getLogin()
                , user.getBirthday()
                , user.getId());
        return user;
    }

    @Override
    public User remove(User user) {

        String sqlQuery = "delete from users where ID = ?";
        jdbcTemplate.update(sqlQuery, user.getId());
        return user;
    }

    @Override
    public void removeAll() {

        String sqlQuery = "delete from users";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public Set<User> getAll() {

        String sqlQuery = "select * from users";
        Set<User> allUsers = new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToUser));
        return allUsers;
    }

    @Override
    public User getById(long userId) {

        final String sqlQuery = "select * from users where ID = ?";
        final List<User> allUsers = jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
        if (allUsers.size() != 0) {
            return allUsers.get(0);
        } else return null;
    }

    @Override
    public User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {

        return User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    private Map<String, Object> toMap(User user) {

        Map<String, Object> values = new HashMap<>();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("birthday", user.getBirthday());
        return values;
    }
}
