package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.UserDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateUserDbTest {

    private final UserDbStorage userStorage;
    private User testUser;

    @BeforeEach
    public void createUser() {
        this.testUser = User.builder()
                .name("Test User")
                .email("test@email.com")
                .login("xxxdddrrr")
                .birthday(LocalDate.of(2000, Month.JANUARY, 10))
                .build();
    }

    @AfterEach
    public void clearAll() {
        userStorage.removeAll();
    }

    @Test
    @DisplayName("should add user")
    public void shouldAddUser() {

        userStorage.add(testUser);
        long testId = testUser.getId();
        Optional<User> optionalUser = Optional.of(testUser);

        assertThat(optionalUser)
                .isPresent().hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", testId));
    }

    @Test
    @DisplayName("should update user")
    public void shouldUpdateUser() {

        testUser.setName("Updated Test User");
        userStorage.update(testUser);
        Optional<User> optionalUser = Optional.of(testUser);

        assertThat(optionalUser)
                .isPresent().hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Updated Test User"));
    }

    @Test
    @DisplayName("should get user by id")
    public void shouldGetUserById() {

        userStorage.add(testUser);
        long testId = testUser.getId();

        Optional<User> optionalUser = Optional.of(userStorage.getById(testId));
        assertThat(optionalUser).isNotEmpty();
    }

    @Test
    @DisplayName("should delete user")
    public void shouldDeleteUser() {

        userStorage.add(testUser);
        userStorage.remove(testUser);

        assertThat(userStorage.getAll()).isEmpty();
    }
}
