package ru.yandex.practicum.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Component("GenreDbStorage")
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void setFilmGenre(Film film) {

        String sqlDeleteQuery =
                "delete from films_genres " +
                        "where FILM_ID = ?";
        jdbcTemplate.update(sqlDeleteQuery, film.getId());
        String sqlInsertQuery = "insert into films_genres (GENRE_ID, FILM_ID) " +
                "values (?, ?)";

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlInsertQuery, genre.getId(), film.getId());
            }
        } else {
            film.setGenres(null);
        }
    }

    @Override
    public Set<Genre> loadFilmGenres(long filmId) {

        String sqlQuery = "select g.ID, g.NAME from films_genres as fg " +
                "join genres as g on g.ID = fg.GENRE_ID " +
                "where fg.FILM_ID = ?";

        Set<Genre> filmGenres = new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId));
        return filmGenres;
    }

    @Override
    public Set<Genre> getAll() {

        String sqlQuery = "select * from genres";
        Set<Genre> allGenres = new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre));
        Set<Genre> sortedGenres = allGenres.stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortedGenres;
    }

    @Override
    public Genre getById(long genreId) {

        final String sqlQuery = "select * from genres where ID = ?";
        final List<Genre> allGenres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, genreId);
        if (allGenres.size() != 0) {
            return allGenres.get(0);
        } else return null;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {

        return Genre.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}