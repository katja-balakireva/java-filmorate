package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ServerErrorException;
import ru.yandex.practicum.exceptions.ValidationException;
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
    public LikeService( @Qualifier("LikeDbStorage") LikeStorage likeStorage,
                        @Qualifier("FilmDbStorage") FilmStorage filmStorage,
                        @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.likeStorage = likeStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long filmId, long likeId) {

        System.out.println("ADD LIKE FROM SERVICE IS CALLED!!!!");

        if (!validateLikes(filmId, likeId)) {
            throw new ValidationException("Не пройдена валидация");
        }
        if (filmStorage.getById(filmId) != null) {
            if (userStorage.getById(likeId) != null) {
                likeStorage.addLike(filmId, likeId);
            } else {
                throw new NotFoundException("Пользователь не найден");
            }
        } else {
            throw new ServerErrorException("Ошибка сервера");
        }
    }

    public void removeLike(long likeId, long filmId) {

        if (!validateLikes(likeId, filmId)) {
            return;
        }

        if (userStorage.getById(likeId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (filmStorage.getById(filmId) != null) {
            likeStorage.removeLike(likeId,filmId);
        } else throw new ServerErrorException("Ошибка сервера");
    }

    public void removeAllLikes(long filmId) {

        if (filmStorage.getById(filmId) != null) {
            likeStorage.removeAllLikes(filmId);
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

    public boolean validateLikes(long filmId, long likeId) {
        if (likeStorage.likesListByFilm(filmId).isEmpty()) {
            log.warn("Список лайков фильма с id {} пуст", filmId);
            throw new NotFoundException("Список лайков фильма пуст");
        }
        if (!likeStorage.likesListByFilm(filmId).contains(likeId)) {
            log.warn("Фильм с id {} не содержит лайк с id {}", filmId, likeId);
            throw new NotFoundException("Лайк не найден");
        }
        if (likeId < 0) {
            log.warn("id лайка не может быть отрицательным: {}", likeId);
            throw new NotFoundException("Лайк не существует");
        }
        return true;
    }
}
