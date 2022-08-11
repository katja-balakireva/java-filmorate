package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ServerErrorException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.FilmStorage;
import ru.yandex.practicum.storage.LikeStorage;
import ru.yandex.practicum.storage.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LikeService {

    @Qualifier("LikeDbStorage")
    private LikeStorage likeStorage;
    @Qualifier("FilmDbStorage")
    private FilmStorage filmStorage;
    @Qualifier("UserDbStorage")
    private UserStorage userStorage;

    @Autowired
    public LikeService( @Qualifier("LikeDbStorage")LikeStorage likeStorage,
                        @Qualifier("FilmDbStorage") FilmStorage filmStorage,
                        @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.likeStorage = likeStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long likeId, long filmId) {

        if (filmStorage.getById(filmId) != null) {
            if (userStorage.getById(likeId) != null) {
                likeStorage.addLike(likeId, filmId);
            } else {
                throw new NotFoundException("Пользователь не найден");
            }
        } else {
            throw new ServerErrorException("Ошибка сервера");
        }
    }

    public void removeLike(long likeId, long filmId) {

        if (userStorage.getById(likeId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (filmStorage.getById(filmId) != null) {

            if (!likeStorage.likesListByFilm(filmId).isEmpty()) {
                if (likeStorage.likesListByFilm(filmId).contains(likeId)) {
                    likeStorage.removeLike(likeId,filmId);
                } else {
                    throw new NotFoundException("Лайк не найден");
                }
            } else {
                throw new NotFoundException("Список лайков пуст");
            }
        } else throw new ServerErrorException("Ошибка сервера");
    }

    public void removeAllLikes(long filmId) {

        if (filmStorage.getById(filmId) != null) {
            if (!likeStorage.likesListByFilm(filmId).isEmpty()) {
                likeStorage.removeAllLikes(filmId);
            } else {
                throw new NotFoundException("Список лайков пуст");
            }
        } else {
            throw new ServerErrorException("Ошибка сервера");
        }
    }

    public int likesCountByFilm(long filmId) {
        return likeStorage.likesCountByFilm(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        return likeStorage.getPopularFilms(count);
    }
}
