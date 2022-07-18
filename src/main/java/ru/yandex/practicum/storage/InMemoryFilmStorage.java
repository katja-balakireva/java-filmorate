package ru.yandex.practicum.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.controllers.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private static final LocalDate RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private static int idCounter = 0;
    private Set<Film> films = new HashSet<>();

    @Override
    public Film add(Film film){

        for (Film f: films) {
            if (f.getId() == film.getId()) {
                throw new ValidationException("Фильм с таким id уже существует");
            }
        }

        if (validateFilm(film)) {
            long newId = ++idCounter;
            film.setId(newId);
            films.add(film);
            //log.info("Добавлен фильм: {}", film.getName());
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
                    break;
                    //  log.info("Фильм {} обновлён.", film.getName());
                }
            }
        }
        return film;
    }

    @Override
    public Film remove(Film film) {

            if (validateFilm(film)) {
                if (films.contains(film)) {
                    films.remove(film);
                }
            }
            return film; //проверить не будет ли ошибки
    }

    @Override
    public Set<Film> getAll() {
        return films;
    }

    @Override
    public Film getById(long filmId) {
        for (Film f: films) {
            if (f.getId() == filmId) {
                return f;
            }
        }
        return null;
    }

    private boolean validateFilm(Film film) throws ValidationException {
        int maxDescriptionLength = 200;

        if (film.getName().isBlank() || film.getName().isEmpty()) {
          //  log.warn("Ошибка валидации названия фильма");
            throw new ValidationException("Пустое название фильма");
        }
        if (film.getDescription().length() > maxDescriptionLength) {
          //  log.warn("Ошибка валидации описания фильма");
            throw new ValidationException(
                    String.format("Описание фильма содержит более %d символов", maxDescriptionLength));
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
          //  log.warn("Ошибка валидации даты релиза фильма");
            throw new ValidationException(
                    String.format("Дата релиза фильма до %s", RELEASE_DATE.toString()));
        }
        if (film.getDuration() < 0) {
          //  log.warn("Ошибка валидации продолжительности фильма");
            throw new ValidationException("Продолжительность фильма отрицательная");
        }
        return true;
    }


}
