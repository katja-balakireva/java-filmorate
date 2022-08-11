package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.storage.GenreStorage;
import ru.yandex.practicum.storage.MpaStorage;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class GenreService {
    @Qualifier("GenreDbStorage")
    private GenreStorage genreStorage;

    public GenreService(@Qualifier("GenreDbStorage")GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Set<Genre> getAllGenres() {
        return genreStorage.getAll();
    }

    public Genre getGenreById(long genreId) {

        if (genreStorage.getById(genreId) != null) {
            return genreStorage.getById(genreId);
        } else throw new NotFoundException("Жанр с таким id не найден");
    }
}
