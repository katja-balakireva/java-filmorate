package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private HashSet<Film> films = new HashSet<>();
    private static int idCounter = 0;

    @GetMapping
    public HashSet<Film> getAllFilms() {
        return films;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {

        if (validateFilm(film)) {
            for (Film f : films) {
                if (f.getId() == film.getId()) {
                    films.remove(f);
                    films.add(film);
                    log.info("Фильм {} обновлён.", film.getName());
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return film;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {

        if (validateFilm(film)) {
            film.setId(++idCounter);
            films.add(film);
            log.info("Добавлен фильм: {}",film.getName());
        }
        return film;
    }

    private boolean validateFilm(Film film) throws ValidationException {

        int maxDescriptionLength = 200;
        LocalDate releaseDate = LocalDate.of(1895, Month.DECEMBER, 28);

        if (film.getName().isBlank() || film.getName().isEmpty()) {
            log.warn("Ошибка валидации названия фильма");
            throw new ValidationException("Пустое название фильма");
        }

        if (film.getDescription().length() > maxDescriptionLength) {
            log.warn("Ошибка валидации описания фильма");
            throw new ValidationException(
                    String.format("Описание фильма содержит более %d символов",maxDescriptionLength));
        }

        if (film.getReleaseDate().isBefore(releaseDate)){
            log.warn("Ошибка валидации даты релиза фильма");
            throw new ValidationException(
                    String.format("Дата релиза фильма до %s",releaseDate.toString()));
        }

        if (film.getDuration() < 0) {
            log.warn("Ошибка валидации продолжительности фильма");
            throw new ValidationException("Продолжительность фильма отрицательная");
        }

        return true;

    }
}
