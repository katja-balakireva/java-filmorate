package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.controllers.FilmController;
import ru.yandex.practicum.controllers.FilmorateApplication;
import ru.yandex.practicum.controllers.UserController;
import ru.yandex.practicum.controllers.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = FilmorateApplication.class)
class FilmorateApplicationTests {

}
