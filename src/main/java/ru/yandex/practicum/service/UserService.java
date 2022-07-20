package ru.yandex.practicum.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.ServerErrorException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Getter
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(long userId, long friendId) {

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (user != null && friend != null) {

            user.setAndCheckFriendsId(friendId);
            friend.setAndCheckFriendsId(userId);

        } else throw new ServerErrorException("Ошибка сервера");
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
                User u = userStorage.getById(id);
                commonFriendsList.add(u);
            }
        }
        return commonFriendsList;
    }
}
