package ru.yandex.practicum.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ServerErrorException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
public class FilmService {
    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(long filmId, long userId) {

        Film film = filmStorage.getById(filmId);

        if (film != null) {
            film.setAndCheckLikes(userId);
        } else throw new ServerErrorException("Ошибка сервера");

    }

    public void removeLike(long filmId, long userId) {

        Film film = filmStorage.getById(filmId);
        if (film != null) {
            Set<Long> filmLikes = film.getLikesId();

            if (filmLikes != null) {

                if (filmLikes.contains(userId)) {
                    filmLikes.remove(userId);
                } else {
                    throw new NotFoundException("Лайк не найден");
                }
            } else {
                throw new NotFoundException("Список лайков пуст");
            }
        } else throw new ServerErrorException("Ошибка сервера");
    }

    public List<Film> getPopularFilms(int count) {

        Map<Film, Integer> newMap = new HashMap<>();

        for (Film f : filmStorage.getAll()) {
            if (f.getLikesId() != null) {
                newMap.put(f, f.getLikesId().size());
            } else {
                newMap.put(f, 0);
            }
        }

        List<Film> sorted = newMap.entrySet().stream()
                .sorted(Map.Entry.<Film, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(count)
                .collect(Collectors.toList());

        return sorted;
    }
}


