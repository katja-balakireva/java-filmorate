package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.exceptions.ServerErrorException;
import ru.yandex.practicum.exceptions.ValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExcHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //code 400
    public Map<String, String> handleValidationException(final ValidationException e) {
        return Map.of("validation error", "Не пройдена валидация");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //code 404
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        return Map.of("not found", "Объект не найден");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //code 500
    public Map<String, String> handleServerErrorException(final ServerErrorException e) {
        return Map.of("error", "Ошибка на сервере");
    }
}
