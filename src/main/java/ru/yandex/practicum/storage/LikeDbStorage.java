package ru.yandex.practicum.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Component("LikeDbStorage")
public class LikeDbStorage implements LikeStorage{

    private final JdbcTemplate jdbcTemplate;
    @Qualifier("UserDbStorage")
    private UserStorage userStorage;
    @Qualifier("FilmDbStorage")
    private FilmStorage filmStorage;

    public LikeDbStorage(JdbcTemplate jdbcTemplate,
                         @Qualifier("UserDbStorage") UserStorage userStorage,
                         @Qualifier("FilmDbStorage")FilmStorage filmStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    @Override
    public void addLike(long filmId, long likeId) {

        User user = userStorage.getById(likeId);
        Film film = filmStorage.getById(filmId);
        String sqlQuery = "insert into likes (LIKE_ID, FILM_ID) " +
                "values (?,?)";

        jdbcTemplate.update(sqlQuery,user.getId(),film.getId());
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

        System.out.println("КОЛИЧЕСТВО ПОП ФИЛЬМОВ: " + popularFilms.size());
        return popularFilms;
    }

    @Override
    public Set<Long> likesListByFilm(long filmId) {
        String sqlQuery = "select LIKE_ID from likes " +
                " where FILM_ID = ?";


//        String sqlQuery = "select LIKE_ID from likes as l join users as u on " +
//                "l.LIKE_ID = u.ID join films as f on l.LIKE_ID = f.ID " +
//                "where f.ID = ?";

        List<Long> allLikesByFilm = jdbcTemplate.queryForList(sqlQuery, Long.class, filmId);
        System.out.println("********* список лайков *************" + allLikesByFilm);
        System.out.println("********* размер списка *************" + allLikesByFilm.size());
        return new HashSet<>(allLikesByFilm);
    }
}
