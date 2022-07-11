package ru.yandex.practicum.controllers;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
