package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.service.GenreService;
import ru.yandex.practicum.service.MpaService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {

    private GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Set<Genre> getAll() {
        Set<Genre> genreList = genreService.getAllGenres();
        log.info("Получен список из жанров размером {}",genreList.size());
        return genreList;
    }

    @GetMapping(value = "{genreId}")
    public Genre getById(@PathVariable long genreId) {
        Genre genre = genreService.getGenreById(genreId);
        log.info("Получен жанр {} с id {}", genre.getName(), genre.getId());
        return genre;
    }
}

