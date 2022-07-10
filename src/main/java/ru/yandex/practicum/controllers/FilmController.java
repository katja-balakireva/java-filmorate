package ru.yandex.practicum.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;

@RestController
@RequestMapping("/films")
public class FilmController {
    private HashSet<Film> films = new HashSet<>();

    @GetMapping
    public HashSet getAllFilms() {
        return films;
    }

    @PutMapping
    //"/films?id=1"
    public Film updateFilm(@RequestParam("id") int id, @RequestBody Film film) throws ValidationException {

        if (validateFilm(film)) {
            for (Film f: films) {
                if (f.getId() == id) {
                    films.add(film);
                }
            }
        }
        return film;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {

        if (validateFilm(film)) {
            films.add(film);
        }
        return film;
    }

    private boolean validateFilm(Film film) throws ValidationException {
        if (film.getName().isBlank() || film.getName().isEmpty()) {
            throw new ValidationException("Ошибка валидации названия фильма");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Ошибка валидации описания фильма");
            }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER,28))) {
                throw new ValidationException("Ошибка валидации даты релиза фильма");
            }
        if (film.getDuration() < 0) {
                throw new ValidationException("Ошибка валидации продолжительности фильма");
            }
        return true;
    }
}
