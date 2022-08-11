package ru.yandex.practicum.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Component("LikeDbStorage")
public class LikeDbStorage implements LikeStorage{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long likeId, long filmId) {
        String sqlQuery = "insert into likes(LIKE_ID, FILM_ID) " +
                "values (?,?)";

        jdbcTemplate.update(sqlQuery,likeId,filmId);
    }

    @Override
    public void removeLike(long likeId, long filmId) {
        String sqlQuery = "delete from likes " +
                "where LIKE_ID = ? and FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,likeId,filmId);
    }

    @Override
    public void removeAllLikes(long filmId) {
        String sqlQuery = "delete from likes " +
                "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,filmId);
    }

    @Override
    public int likesCountByFilm(long filmId) {

        String sqlQuery = "select count(LIKE_ID) from likes " +
                " where FILM_ID = ?";

         return jdbcTemplate.queryForObject(sqlQuery, Integer.class,filmId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlQuery = "select ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, " +
                "count(l.LIKE_ID) from films as f " +
                "join likes as l on l.FILM_ID = f.ID " +
                "group by f.ID order by count(l.LIKE_ID) desc limit ?";

        List <Film> popularFilms = jdbcTemplate.query(sqlQuery, (resultSet, rowNumber) -> new Film(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getInt("duration")), count);
        return popularFilms;
    }

    @Override
    public Set<Long> likesListByFilm(long filmId) {
        String sqlQuery = "select LIKE_ID from likes " +
                " where FILM_ID = ?";

        List<Long> allLikesByFilm = jdbcTemplate.queryForList(sqlQuery, Long.class);
        return new HashSet<>(allLikesByFilm);
    }
}
