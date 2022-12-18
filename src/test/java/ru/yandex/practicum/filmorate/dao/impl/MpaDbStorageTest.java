package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;



    @Test
    void findAll() {
        List<Mpa> mpas = mpaDbStorage.findAll();

        assertEquals(mpas.size(), 5);
    }

    @Test
    void getMpaById() {
        Mpa mpa = mpaDbStorage.getMpaById(1);

        assertThat(mpa)
                .hasFieldOrPropertyWithValue("name", "G");
    }
}