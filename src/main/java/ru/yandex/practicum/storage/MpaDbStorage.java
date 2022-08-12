package ru.yandex.practicum.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Component("MpaDbStorage")
public class MpaDbStorage implements MpaStorage{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa add(Mpa rating) {

        String sqlQuery = "insert into ratings(NAME) " +
                "values (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, rating.getName());
            return stmt;
        }, keyHolder);

        long ratingId = keyHolder.getKey().longValue();
        return getById(ratingId);
    }

    @Override
    public Mpa update(Mpa rating) {

        String sqlQuery = "update ratings set " +
                "NAME = ? " +  "where ID = ?";
        jdbcTemplate.update(sqlQuery
                , rating.getName()
                , rating.getId());
        return rating;
    }

    @Override
    public Mpa remove(Mpa rating) {

        String sqlQuery = "delete from ratings where ID = ?";
        jdbcTemplate.update(sqlQuery, rating.getId());
        return rating;
    }

    @Override
    public List<Mpa> getAll() {

        String sqlQuery = "select * from ratings";
        List<Mpa> allRatings = new ArrayList<>(jdbcTemplate.query(sqlQuery, this::mapRowToMpa));
        List<Mpa> sortedRatings = allRatings.stream()
                .sorted(Comparator.comparing(Mpa::getId))
                .collect(Collectors.toList());
        return sortedRatings;
    }


    @Override
    public Mpa getById(long ratingId) {

        final String sqlQuery = "select * from ratings where ID = ?";
        final List<Mpa> allRatings = jdbcTemplate.query(sqlQuery, this::mapRowToMpa, ratingId);
        if (allRatings.size() != 0) {
            return allRatings.get(0);
        } else return null;
}

    @Override
    public Mpa loadFilmMpa(long filmId) {
        String sqlQuery = "select r.ID, r.NAME from ratings as r " +
                "join films as f on r.ID = f.RATING_ID " +
                "where f.ID = ?";

        final List<Mpa> allRatings = jdbcTemplate.query(sqlQuery, this::mapRowToMpa, filmId);
        if (allRatings.size() != 0) {
            return allRatings.get(0);
        } else return null;
    }

//    @Override
//    public void setFilmMpa(Film film) {
//        String sqlDeleteQuery =
//                "delete RATING_MPA from films " +
//                        "where ID = ?";
//        jdbcTemplate.update(sqlDeleteQuery, film.getId());
//        String sqlInsertQuery = "insert into films_genres (GENRE_ID, FILM_ID) " +
//                "values (?, ?)";
//
//        if (getAll() != null || !getAll().isEmpty()) {
//            for (Genre genre: getAll()) {
//                jdbcTemplate.update(sqlInsertQuery, genre.getId(),film.getId());
//            }
//        }
//    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
