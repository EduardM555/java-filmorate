package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserStorage userStorage;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Name");
        user.setLogin("Login");
        user.setEmail("user@mail.ru");
        user.setBirthday(LocalDate.of(2000, 12, 1));
        userStorage.save(user);
    }

    @AfterEach
    void tearDown() {
        userStorage.delete(user.getId());
    }

    @Test
    void findAll() {
        List<User> newUsers = userStorage.findAll();

        assertNotNull(newUsers);
        assertEquals(user, newUsers.get(0));

    }

    @Test
    void update() {
        user.setName("New Name");
        userStorage.update(user);
        User newUser = userStorage.findUserById(user.getId());

        assertEquals("New Name", user.getName());

        assertThat(newUser)
                .hasFieldOrPropertyWithValue("name", user.getName());
    }

    @Test
    void findUserById() {
        User byId = userStorage.findUserById(user.getId());

        assertNotNull(byId);
        assertEquals(user.getId(), byId.getId());
        assertEquals(user.getEmail(), byId.getEmail());

        assertThat(byId)
                .hasFieldOrPropertyWithValue("id", user.getId())
                .hasFieldOrPropertyWithValue("email", user.getEmail());
    }
}