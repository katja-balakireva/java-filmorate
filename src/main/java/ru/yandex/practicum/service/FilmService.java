package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FilmService {
    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(long filmId, long userId) {

        Film film = filmStorage.getById(filmId);
        Set<Long> filmLikes = film.getLikesId();

        if (!filmLikes.contains(userId)) { //можно убрать
            filmLikes.add(userId);
        }
    }

    public void removeLike(long filmId, long userId) {

        Film film = filmStorage.getById(filmId);
        Set<Long> filmLikes = film.getLikesId();

        if (filmLikes.contains(userId)) { // /можно убрать
            filmLikes.remove(userId);
        }
    }

    public List<Map.Entry<Integer, Film>> getPopularFilms() {

        Set<Film> allFilms = filmStorage.getAll();

        Map<Integer, Film> likesMap = new TreeMap<>(Collections.reverseOrder());

        for (Film f : allFilms) {
            likesMap.put(f.getLikesId().size(), f);
        }

        List<Map.Entry<Integer, Film>> entriesStream = likesMap.entrySet().stream()
                .limit(10)
                .collect(Collectors.toList());

        return entriesStream;

    }
}

