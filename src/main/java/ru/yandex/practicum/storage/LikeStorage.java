package ru.yandex.practicum.storage;

import java.util.Set;

public interface LikeStorage {

    void addLike(long filmId, long likeId);

    void removeLike(long likeId, long filmId);

    Integer likesCountByFilm(long filmId);

    Set<Long> likesListByFilm(long filmId);

    void removeAllLikes(long filmId);
}
