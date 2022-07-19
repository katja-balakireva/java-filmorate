package ru.yandex.practicum.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ServerErrorException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        Set<Film> allFilms = filmStorage.getAll();

        Map<Integer, Film> likesMap = new TreeMap<>(Comparator.reverseOrder());

        for (Film f : allFilms) {
            if (f.getLikesId()!= null) {
                likesMap.put(f.getLikesId().size(), f);
            } else {
                likesMap.put(0, f);
            }
        }

        List<Film> valuesList = new ArrayList<>(likesMap.values());

        if (valuesList.size() >= count) {
            return valuesList.subList(0,count -1);
        } else {
            return valuesList;
        }

    }
}

