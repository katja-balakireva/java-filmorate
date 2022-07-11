package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.controllers.FilmController;
import ru.yandex.practicum.controllers.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerValidationTests {

    FilmController filmController;
    Film film;

    @BeforeEach
    public void init() {
        filmController = new FilmController();
        film = Film.builder()
                .id(1)
                .name("filmTitle")
                .description("filmDescription")
                .releaseDate(LocalDate.of(2000, Month.DECEMBER, 25))
                .duration(120)
                .build();
    }

    @Test
    @DisplayName("should throw exception if invalid film name")
    void shouldThrowExceptionIfInvalidFilmName() throws ValidationException {

        filmController.addFilm(film);
        assertEquals(filmController.getAllFilms().size(), 1, "Фильм не добавлен в список");

        film.setName("");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(film));
        assertEquals("Пустое название фильма", exception.getMessage());
    }

    @Test
    @DisplayName("should throw exception if invalid film description")
    void shouldThrowExceptionIfInvalidFilmDescription() throws ValidationException {

        filmController.addFilm(film);
        assertEquals(filmController.getAllFilms().size(), 1, "Фильм не добавлен в список");

        String longDescription = "d".repeat(300);

        film.setDescription(longDescription);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(film));

        assertEquals("Описание фильма содержит более 200 символов", exception.getMessage());
    }

    @Test
    @DisplayName("should throw exception if invalid release date")
    void shouldThrowExceptionIfInvalidReleaseDate() throws ValidationException {

        filmController.addFilm(film);
        assertEquals(filmController.getAllFilms().size(), 1, "Фильм не добавлен в список");

        LocalDate releaseDate = LocalDate.of(1895, Month.DECEMBER, 28);
        LocalDate wrongDate = LocalDate.of(1880, Month.DECEMBER, 10);

        film.setReleaseDate(wrongDate);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(film));

        assertEquals(String.format("Дата релиза фильма до %s", releaseDate.toString()), exception.getMessage());
    }

    @Test
    @DisplayName("should not add empty film object to the list")
    void shouldNotAddEmptyFilmObjectToTheList() {
        Film emptyFilm = Film.builder()
                .build();

        assertThrows(
                NullPointerException.class,
                () -> filmController.addFilm(emptyFilm));

        assertTrue(filmController.getAllFilms().isEmpty(), "Пустой фильм добавлен в список");
    }
}