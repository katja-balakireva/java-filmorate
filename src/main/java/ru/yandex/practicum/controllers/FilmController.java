package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ServerErrorException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.service.LikeService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private FilmService filmService;
    private LikeService likeService;

    @Autowired
    public FilmController(FilmService filmService, LikeService likeService) {
        this.filmService = filmService;
        this.likeService = likeService;
    }

    @GetMapping
    public Set<Film> getAllFilms() {
        Set<Film> filmList = filmService.getAllFilms();
        log.info("Получен список из {} фильмов", filmList.size());
        return filmList;
    }

    @GetMapping(value = "{filmId}")
    public Film getById(@PathVariable long filmId) {
        Film film = filmService.getFilmById(filmId);
        log.info("Получен фильм {} с id {}", film.getName(), film.getId());
        return film;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        Film addedFilm = filmService.addFilm(film);
        log.info("Добавлен фильм {} с id {}", film.getName(), film.getId());
        return addedFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        Film updatedFilm = filmService.updateFilm(film);
        log.info("Информация о фильме {} обновлена.", updatedFilm.getName());
        return updatedFilm;
    }

    @DeleteMapping
    public void deleteFilm(@RequestBody Film film) {
        log.info("Фильм {} с id {} удалён", film.getName(), film.getId());
        filmService.removeFilm(film);
    }

    @PutMapping(value = "{id}/like/{userId}")
    public List<Long> addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь c id {} поставил лайк фильму с id {}", userId, id);
        return likeService.addLike(id, userId);
    }

    @DeleteMapping(value = "{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь c id {} удалил лайк у фильма {}", userId, id);
        likeService.removeLike(id, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10",
            required = false) int count) {
        List<Film> popularFilms = likeService.getPopularFilms(count);
        log.info("Получен список {} самых популярных фильмов: {} ", popularFilms.size(), popularFilms);
        return popularFilms;
    }
}
