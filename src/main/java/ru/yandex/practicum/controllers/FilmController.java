package ru.yandex.practicum.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;

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
    public Film updateFilm(@RequestParam("id") int id, @RequestBody Film film) {

        for (Film f: films) {
            if (f.getId() == id) {
                films.add(film);
            }
        }
        return film;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        films.add(film);
        return film;
    }
}
