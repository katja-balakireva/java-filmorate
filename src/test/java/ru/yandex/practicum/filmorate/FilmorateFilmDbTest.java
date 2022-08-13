package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.storage.FilmDbStorage;
import ru.yandex.practicum.storage.GenreDbStorage;
import ru.yandex.practicum.storage.MpaDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateFilmDbTest {

    private final FilmDbStorage filmStorage;
    private final MpaDbStorage mpaStorage;
    private final GenreDbStorage genreStorage;
    private Film testFilm;

    @BeforeEach
    public void createFilm() {

        this.testFilm = Film.builder()
                .name("Test Film")
                .description("test film description")
                .releaseDate(LocalDate.of(1990, Month.JULY, 20))
                .duration(120)
                .mpa(mpaStorage.getById(1))
                .build();
    }

    @AfterEach
    public void clearAll() {
        filmStorage.removeAll();
    }

    @Test
    @DisplayName("should add film")
    public void shouldAddFilm() {

        filmStorage.add(testFilm);
        long testId = testFilm.getId();
        Optional<Film> optionalFilm = Optional.of(testFilm);

        assertThat(optionalFilm)
                .isPresent().hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", testId));
    }

    @Test
    @DisplayName("should update film")
    public void shouldUpdateFilm() {

        testFilm.setDuration(100);
        filmStorage.update(testFilm);
        Optional<Film> optionalFilm = Optional.of(testFilm);

        assertThat(optionalFilm)
                .isPresent().hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 100));
    }

    @Test
    @DisplayName("should get film by id")
    public void shouldGetFilmById() {

        filmStorage.add(testFilm);
        long testId = testFilm.getId();
        Optional<Film> optionalFilm = Optional.of(filmStorage.getById(testId));

        assertThat(optionalFilm).isNotEmpty();
    }

    @Test
    @DisplayName("should delete film")
    public void shouldDeleteFilm() {

        filmStorage.add(testFilm);
        filmStorage.remove(testFilm);

        assertThat(filmStorage.getAll()).isEmpty();
    }

    @Test
    @DisplayName("should load film genres")
    public void shouldLoadFilmGenres() {

        Set<Genre> testGenres = genreStorage.loadFilmGenres(testFilm.getId());
        assertThat(testGenres).isEmpty();
    }

    @Test
    @DisplayName("should load film mpa")
    public void shouldLoadFilmMpa() {

        Optional<Mpa> optionalMpa = Optional.of(testFilm.getMpa());

        assertThat(optionalMpa)
                .isPresent().hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G"));
    }
}
