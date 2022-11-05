package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    @Autowired
    UserController userController;
    User user1 = new User(
            "user1@mail.ru",
            "user1Login",
            "user1Name",
            LocalDate.of(1983, 4, 26));
    User user2 = new User(
            "user2@mail.ru",
            "user2Login",
            "user2Name",
            LocalDate.of(1987, 7, 27));

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void findAll() {
        assertEquals(0, userController.findAll().size());
        userController.getUsers().put(1, user1);
        userController.getUsers().put(2, user2);
        assertEquals(2, userController.findAll().size());
    }

    @Test
    void create() {
        userController.create(user1);

        assertEquals(user1, userController.getUsers().get(1));
    }

    @Test
    void putUser() {
        userController.putUser(user1);

        assertEquals(user1, userController.getUsers().get(1));
    }

    @Test
    void createUserWithBlankEmail() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(new User(
                        "",
                        "user3Login",
                        "user3Name",
                        LocalDate.of(2000, 1, 1))));

        assertEquals("Ошибка валидации пользователя.", exception.getMessage());
    }

    @Test
    void createUserWithBlankName() {
        User user = userController.create(new User(
                "user3mail.ru@",
                "user3Login",
                " ",
                LocalDate.of(2000, 1, 1)));

        assertEquals("user3Login", user.getName());
    }

    @Test
    void createUserWithWrongEmail() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(new User(
                        "user3.mail.ru",
                        "user3Login",
                        "user3Name",
                        LocalDate.of(2000, 1, 1))));

        assertEquals("Ошибка валидации пользователя.", exception.getMessage());
    }

    @Test
    void createUserWithWrongBirthday() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(new User(
                        "user3@mail.ru",
                        "user3Login",
                        "user3Name",
                        LocalDate.of(2100, 1, 1))));

        assertEquals("Ошибка валидации пользователя.", exception.getMessage());
    }

    @Test
    void createUserWithWrongLogin() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(new User(
                        "user3@mail.ru",
                        "user3 Login",
                        "user3Name",
                        LocalDate.of(2000, 1, 1))));

        assertEquals("Ошибка валидации пользователя.", exception.getMessage());
    }
}