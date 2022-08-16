package ru.yandex.practicum.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Component("LikeDbStorage")
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long filmId, long likeId) {

        String sqlQuery = "merge into LIKES " +
                "values (?,?)";
        jdbcTemplate.update(sqlQuery, likeId, filmId);
    }

    @Override
    public void removeLike(long likeId, long filmId) {

        String sqlQuery = "delete from likes " +
                "where LIKE_ID = ? and FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, likeId, filmId);
    }

    @Override
    public void removeAllLikes(long filmId) {

        String sqlQuery = "delete from likes " +
                "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public Integer likesCountByFilm(long filmId) {

        String sqlQuery = "select count(l.LIKE_ID) from likes as l " +
                "join films as f on l.LIKE_ID = f.ID " +
                "where l.FILM_ID = ?";

        Integer likesCount = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId);

        if (likesCount == null || likesCount == 0) {
            return 0;
        } else return likesCount;
    }

    @Override
    public Set<Long> likesListByFilm(long filmId) {

        String sqlQuery = "select LIKE_ID from likes " +
                " where FILM_ID = ?";

        List<Long> allLikesByFilm = jdbcTemplate.queryForList(sqlQuery, Long.class, filmId);
        return new HashSet<>(allLikesByFilm);
    }
}
