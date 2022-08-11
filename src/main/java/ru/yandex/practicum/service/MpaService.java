package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.storage.MpaStorage;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class MpaService {
    @Qualifier("MpaDbStorage")
    private MpaStorage mpaStorage;

    public MpaService(@Qualifier("MpaDbStorage")MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAll();
    }

   public Mpa getMpaById(long mpaId) {

            if (mpaStorage.getById(mpaId) != null) {
                return mpaStorage.getById(mpaId);
            } else throw new NotFoundException("Рейтинг с таким id не найден");
        }
   }

