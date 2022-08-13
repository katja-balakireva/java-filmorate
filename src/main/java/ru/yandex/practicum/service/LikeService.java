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

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LikeService {

    @Qualifier("LikeDbStorage")
    private LikeStorage likeStorage;
    @Qualifier("FilmDbStorage")
    private FilmStorage filmStorage;

    @Autowired
    public LikeService(@Qualifier("LikeDbStorage") LikeStorage likeStorage,
                       @Qualifier("FilmDbStorage") FilmStorage filmStorage) {
        this.likeStorage = likeStorage;
        this.filmStorage = filmStorage;
    }

    public void addLike(long filmId, long likeId) {

        if (validateLikes(likeId, filmId)) {
            likeStorage.addLike(filmId, likeId);
        }
    }

    public void removeLike(long likeId, long filmId) {

        if (validateLikes(likeId, filmId)) {
            likeStorage.removeLike(likeId, filmId);
        }
    }

    public void removeAllLikes(long filmId) {

        if (filmStorage.getById(filmId) != null) {
            likeStorage.removeAllLikes(filmId);
        } else {
            throw new ServerErrorException("Ошибка сервера");
        }
    }

    public List<Film> getPopularFilms(int count) {

        Set<Film> allFilms = filmStorage.getAll();
        Map<Film, Integer> likesMap = new HashMap<>();

        for (Film film : allFilms) {
            likesMap.put(film, likeStorage.likesCountByFilm(film.getId()));
        }

        List<Film> sorted = likesMap.entrySet().stream()
                .sorted(Map.Entry.<Film, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(count)
                .collect(Collectors.toList());

        return sorted;
    }

    public boolean validateLikes(long filmId, long likeId) {

        if (likeId < 0 || filmId < 0) {
            log.warn("id лайка или фильма не может быть отрицательным: {}, {}", likeId, filmId);
            throw new NotFoundException("Лайк или фильм не существует");
        }
        return true;
    }
}