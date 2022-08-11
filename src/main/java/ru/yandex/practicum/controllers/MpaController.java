package ru.yandex.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.service.MpaService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {

    private MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<Mpa> getAll() {
        List<Mpa> mpaList = mpaService.getAllMpa();
        log.info("Получен список из рейтингов размером {}", mpaList.size());
        return mpaList;
    }

    @GetMapping(value = "{mpaId}")
    public Mpa getById(@PathVariable long mpaId) {
        Mpa mpa = mpaService.getMpaById(mpaId);
        log.info("Получен рейтинг {} с id {}", mpa.getName(), mpa.getId());
        return mpa;
    }

}
