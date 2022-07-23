package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ServerErrorException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private static final LocalDate RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Set<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(long filmId) {
        if (filmStorage.getById(filmId) != null) {
            return filmStorage.getById(filmId);
        } else throw new NotFoundException("Фильм с таким id не найден");
    }

    public Film addFilm(Film filmToAdd) {
        for (Film film : filmStorage.getAll()) {
            if (film.getId() == filmToAdd.getId()) {
                throw new ValidationException("Фильм с таким id уже существует");
            }
        }
        if (validateFilm(filmToAdd)) {
            return filmStorage.add(filmToAdd);
        }
        return filmToAdd;
    }

    public Film updateFilm(Film filmToUpdate) {
        if (validateFilm(filmToUpdate)) {
            for (Film film : filmStorage.getAll()) {
                if (film.getId() == filmToUpdate.getId()) {
                    return filmStorage.update(filmToUpdate);
                }
            }
            throw new NotFoundException("Фильм с таким id не найден");
        } else {
            throw new ValidationException("Невозможно обновить информацию о фильме");
        }
    }

    public void removeFilm(Film filmToRemove) {
        if (validateFilm(filmToRemove)) {
            if (filmStorage.getAll().contains(filmToRemove)) {
                filmStorage.remove(filmToRemove);
            } else {
                throw new NotFoundException("Такого фильма нет в списке фильмов");
            }
        }
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

        for (Film film : filmStorage.getAll()) {
            if (film.getLikesId() != null) {
                newMap.put(film, film.getLikesId().size());
            } else {
                newMap.put(film, 0);
            }
        }

        List<Film> sorted = newMap.entrySet().stream()
                .sorted(Map.Entry.<Film, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(count)
                .collect(Collectors.toList());

        return sorted;
    }

    private boolean validateFilm(Film film) throws ValidationException {
        int maxDescriptionLength = 200;

        if (film.getName().isBlank() || film.getName().isEmpty()) {
            log.warn("Ошибка валидации названия фильма: {}", film);
            throw new ValidationException("Пустое название фильма");
        }
        if (film.getDescription().length() > maxDescriptionLength) {
            log.warn("Ошибка валидации описания фильма: {}", film);
            throw new ValidationException(
                    String.format("Описание фильма содержит более %d символов", maxDescriptionLength));
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            log.warn("Ошибка валидации даты релиза фильма: {}", film);
            throw new ValidationException(
                    String.format("Дата релиза фильма до %s", RELEASE_DATE.toString()));
        }
        if (film.getDuration() < 0) {
            log.warn("Ошибка валидации продолжительности фильма: {}", film);
            throw new ValidationException("Продолжительность фильма отрицательная");
        }
        return true;
    }
}


