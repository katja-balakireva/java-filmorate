package ru.yandex.practicum.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ServerErrorException;
import ru.yandex.practicum.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Component("FriendDbStorage")
public class FriendDbStorage implements FriendStorage{
    private final JdbcTemplate jdbcTemplate;

    @Qualifier("UserDbStorage")
    private UserStorage userStorage;

    public FriendDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }
@Override
    public void addFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        String sqlQuery = "insert into friends (USER_ID, FRIEND_ID) " +
                "values (?, ?)";

        if (user != null && friend != null) {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } else throw new NotFoundException("Пользователь не найден");
    }
    @Override
    public void removeFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        String sqlQuery = "delete from friends where USER_ID = ? and FRIEND_ID = ?";
        if (user != null && friend != null) {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } else throw new ServerErrorException("Ошибка сервера");
    }
    @Override
    public List<User> getAllFriends(long userId) {

        String sqlQuery = "select * from friends as f join users as u on " +
                "f.FRIEND_ID = u.ID where f.USER_ID = ?";


        List<User> allFriends = jdbcTemplate.query(sqlQuery, userStorage::mapRowToUser, userId);
        return allFriends;
    }
    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        List<User> userFriendsList = getAllFriends(userId);
        List<User> otherFriendsList = getAllFriends(otherId);
        List<User> commonFriendsList = new ArrayList<>();

        if (userFriendsList != null && otherFriendsList != null) {
            List<User> temp = new ArrayList<>(userFriendsList);
            temp.retainAll(otherFriendsList);
            commonFriendsList.addAll(temp);
        }
        return commonFriendsList;
    }
}
