package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.User;

import java.util.List;

public interface FriendStorage {

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    List<User> getAllFriends(long userId);

    List<User> getCommonFriends(long userId, long otherId);

}
