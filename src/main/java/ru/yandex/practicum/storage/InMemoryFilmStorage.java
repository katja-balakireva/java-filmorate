package ru.yandex.practicum.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private static final LocalDate RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private static int idCounter = 0;
    private Set<Film> films = new HashSet<>();

    @Override
    public Film add(Film film) {

        for (Film f : films) {
            if (f.getId() == film.getId()) {
                throw new ValidationException("Фильм с таким id уже существует");
            }
        }

        if (validateFilm(film)) {
            long newId = ++idCounter;
            film.setId(newId);
            films.add(film);
        }
        return film;
    }

    @Override
    public Film update(Film film) {

        if (validateFilm(film)) {
            for (Film f : films) {
                if (f.getId() == film.getId()) {
                    films.remove(f);
                    films.add(film);
                    return film;
                }
            }
            throw new NotFoundException("Фильм с таким id не найден");
        } else {
            throw new ValidationException("Невозможно обновить информацию о фильме");
        }
    }

    @Override
    public Film remove(Film film) {

        if (validateFilm(film)) {
            if (films.contains(film)) {
                films.remove(film);
            } else {
                throw new NotFoundException("Такого фильма нет в списке фильмов");
            }
        }
        return film;
    }

    @Override
    public Set<Film> getAll() {
        return films;
    }

    @Override
    public Film getById(long filmId) {
        for (Film f : films) {
            if (f.getId() == filmId) {
                return f;
            }
        }
        throw new NotFoundException("Фильм с таким id не найден");
    }

    private boolean validateFilm(Film film) throws ValidationException {
        int maxDescriptionLength = 200;

        if (film.getName().isBlank() || film.getName().isEmpty()) {
            log.warn("Ошибка валидации названия фильма");
            throw new ValidationException("Пустое название фильма");
        }
        if (film.getDescription().length() > maxDescriptionLength) {
            log.warn("Ошибка валидации описания фильма");
            throw new ValidationException(
                    String.format("Описание фильма содержит более %d символов", maxDescriptionLength));
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            log.warn("Ошибка валидации даты релиза фильма");
            throw new ValidationException(
                    String.format("Дата релиза фильма до %s", RELEASE_DATE.toString()));
        }
        if (film.getDuration() < 0) {
            log.warn("Ошибка валидации продолжительности фильма");
            throw new ValidationException("Продолжительность фильма отрицательная");
        }
        return true;
    }
}
