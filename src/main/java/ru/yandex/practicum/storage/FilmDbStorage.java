package ru.yandex.practicum.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage{

    private final JdbcTemplate jdbcTemplate;
    @Qualifier("GenreDbStorage")
    private GenreStorage genreStorage;
    @Qualifier("RatingDbStorage")
    private MpaStorage mpaStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         @Qualifier("GenreDbStorage") GenreStorage genreStorage,
                         @Qualifier("MpaDbStorage") MpaStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Film add(Film film) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        long filmId = simpleJdbcInsert.executeAndReturnKey(toMap(film)).longValue();
        film.setId(filmId);
        genreStorage.setFilmGenre(film);
        String sqlUpdateQuery = "update films set RATING_ID = ? "
                + "where ID = ?";
        jdbcTemplate.update(sqlUpdateQuery, film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {

        String sqlQuery = "update films set " +
                "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, " +
                "DURATION = ?, RATING_ID = ? " +
                "where ID = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());
        genreStorage.setFilmGenre(film);
        if (film.getGenres() == null) {
            film.setGenres(null);
        }
        return film;

//        String sqlDeleteQuery = "delete from films_genres where FILM_ID = ?";
//        jdbcTemplate.update(sqlDeleteQuery, film.getId());
//
//        String sqlInsertQuery = "insert into films_genres (GENRE_ID, FILM_ID) " +
//                "values (?, ?)";
//
//        if (film.getGenres() == null || film.getGenres().isEmpty()) {
//            return film;
//        } else {
//            Set<Genre> filmGenres = genreStorage.loadFilmGenres(film.getId());
//            for (Genre genre : filmGenres) {
//                jdbcTemplate.update(sqlInsertQuery, genre.getId(), film.getId());
//            }
//            genreStorage.setFilmGenre(film);
//            return film;
//        }
    }

    @Override
    public Film remove(Film film) {

        String sqlQuery = "delete from films where ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        return film;
    }

    @Override
    public Set<Film> getAll() {
        String sqlQuery = "select * from films";
        Set<Film> allFilms = new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToFilm));
        return allFilms;
    }

    @Override
    public Film getById(long filmId) {

        final String sqlQuery = "select * from films where ID = ?";
        final List<Film> allFilms = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, filmId);
        //  genreStorage.loadFilmGenres(filmId);
//        String sqlInsertQuery = "insert into films_genres (GENRE_ID, FILM_ID) " +
//                "values (?, ?)";
        if (allFilms.size() != 0) {
            Film film = allFilms.get(0);
            return film;
        } else {
            return null;
        }
//            if (genreStorage.loadFilmGenres(filmId).size() != 0) {
//                film.setGenres(genreStorage.loadFilmGenres(filmId));
//                return film;
//            } else return null;
//        } else return null;
    }

    //mappers
    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {

        return
                Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpaStorage.loadFilmMpa(resultSet.getLong("id")))
                .genres(genreStorage.loadFilmGenres(resultSet.getLong("id")))
                .build();
    }

    private Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name",film.getName());
        values.put("description",film.getDescription());
        values.put("release_date",film.getReleaseDate());
        values.put("duration",film.getDuration());
        return values;
    }
}
