package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    void remove(Film film);

    void removeAll();

    Set<Film> getAll();

    Film getById(long filmId);

    Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException;
}
