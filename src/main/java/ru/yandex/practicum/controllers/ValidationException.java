package ru.yandex.practicum.controllers;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
