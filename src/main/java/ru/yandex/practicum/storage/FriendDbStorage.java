package ru.yandex.practicum.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.User;

import java.util.ArrayList;
import java.util.List;

@Repository
@Component("FriendDbStorage")
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    @Qualifier("UserDbStorage")
    private UserStorage userStorage;

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbcTemplate,
                           @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(long userId, long friendId) {

        String sqlQuery = "insert into friends (USER_ID, FRIEND_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {

        String sqlQuery = "delete from friends where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
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
