package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.controllers.UserController;
import ru.yandex.practicum.controllers.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerValidationTests {

    private UserController userController;
    private User user;

    @BeforeEach
    public void init() {
        userController = new UserController();
        user = User.builder()
                .id(1)
                .email("test@test")
                .login("userLogin")
                .name("userName")
                .birthday(LocalDate.of(2000, Month.APRIL, 2))
                .build();
    }

    @Test
    @DisplayName("should set login as name if empty user name")
    void shouldSetLoginAsNameIfEmptyUserName() throws ValidationException {

        user.setName("");
        userController.addUser(user);
        assertEquals(user.getName(), user.getLogin(), "Логин и email не совпадают в случае пустого логина");
    }

    @Test
    @DisplayName("should throw exception if invalid user email")
    void shouldThrowExceptionIfInvalidUserEmail() {

        user.setEmail("testtest");
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user));
        assertEquals("email пользователя не содержит @", exception1.getMessage());

        user.setEmail("");
        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> userController.updateUser(user));
        assertEquals("Пустой email пользователя", exception2.getMessage());
    }

    @Test
    @DisplayName("should throw exception if invalid user login")
    void shouldThrowExceptionIfInvalidUserLogin() {

        user.setLogin("");
        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user));
        assertEquals("Пустой логин пользователя", exception1.getMessage());

        user.setLogin("user Login");
        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> userController.updateUser(user));
        assertEquals("Логин пользователя содержит пробелы", exception2.getMessage());
    }

    @Test
    @DisplayName("should throw exception if invalid user birthday")
    void shouldThrowExceptionIfInvalidUserBirthday() throws ValidationException {

        user.setBirthday(LocalDate.of(2030, Month.APRIL, 2));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user));
        assertEquals("Дата рождения пользователя в будущем", exception.getMessage());
    }

    @Test
    @DisplayName("should not add empty user object to the list")
    void shouldNotAddEmptyUserObjectToTheList() {
        User emptyUser = User.builder()
                .build();

        assertThrows(
                NullPointerException.class,
                () -> userController.addUser(emptyUser));

        assertTrue(userController.getAllUsers().isEmpty(), "Пустой пользователь добавлен в список");
    }
}
