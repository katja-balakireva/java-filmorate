package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.Film;

import java.util.List;
import java.util.Set;

public interface LikeStorage {

    List<Long> addLike(long likeId, long filmId);
    void removeLike(long likeId, long filmId);
    int likesCountByFilm(long filmId);
    Set<Long> likesListByFilm(long filmId);
    void removeAllLikes(long filmId);
    List<Film> getPopularFilms(int count);
}
