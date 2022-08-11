package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {

    void setFilmGenre(Film film);

    Set<Genre> loadFilmGenres(long filmId);

    Genre add(Genre genre);

    Genre update(Genre genre);

    Genre remove(Genre genre);

    Set<Genre> getAll();

    Genre getById(long genreId);
}

