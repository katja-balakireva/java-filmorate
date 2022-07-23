package ru.yandex.practicum.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.Film;

import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static int idCounter = 0;
    private Set<Film> films = new HashSet<>();

    @Override
    public Film add(Film filmToAdd) {
        long newId = ++idCounter;
        filmToAdd.setId(newId);
        films.add(filmToAdd);
        return filmToAdd;
    }

    @Override
    public Film update(Film filmToUpdate) {
        for (Film film : films) {
            if (film.getId() == filmToUpdate.getId()) {
                films.remove(film);
                films.add(filmToUpdate);
            }
        }
        return filmToUpdate;
    }

    @Override
    public Film remove(Film filmToRemove) {
        films.remove(filmToRemove);
        return filmToRemove;
    }

    @Override
    public Set<Film> getAll() {
        return films;
    }

    @Override
    public Film getById(long filmId) {
        for (Film film : films) {
            if (film.getId() == filmId) {
                return film;
            }
        }
        return null;
    }
}
