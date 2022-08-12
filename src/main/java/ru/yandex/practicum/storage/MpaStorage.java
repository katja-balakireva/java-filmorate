package ru.yandex.practicum.storage;

import ru.yandex.practicum.model.Mpa;

import java.util.List;
import java.util.Set;

public interface MpaStorage {
    Mpa add(Mpa rating);

    Mpa update(Mpa rating);

    Mpa remove(Mpa rating);

    List<Mpa> getAll();

    Mpa getById(long ratingId);

    Mpa loadFilmMpa(long filmId);
}
