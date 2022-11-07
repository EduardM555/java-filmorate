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
    private UserController userController;
    private User user1 = new User(
            "user1@mail.ru",
            "user1Login",
            "user1Name",
            LocalDate.of(1983, 4, 26));
    private User user2 = new User(
            "user2@mail.ru",
            "user2Login",
            "user2Name",
            LocalDate.of(1987, 7, 27));
    private User user3 = new User(
            "user3@mail.ru",
            "user3Login",
            "user3Name",
            LocalDate.of(2000, 1, 1));

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void findAllAndCreate() {
        assertEquals(0, userController.findAll().size());
        userController.create(user1);
        userController.create(user2);

        assertEquals(user1, userController.getUsers().get(user1.getId()));
        assertEquals(2, userController.findAll().size());
    }

    @Test
    void putUser() {
        userController.create(user3);
        user3.setName("user1NewName");
        userController.putUser(user3);

        assertEquals(user3, userController.getUsers().get(user3.getId()));
    }

    @Test
    void createUserWithBlankEmail() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(new User(
                        "",
                        "user4Login",
                        "user4Name",
                        LocalDate.of(2001, 1, 1))));

        assertEquals("Ошибка валидации пользователя при создании.", exception.getMessage());
    }

    @Test
    void createUserWithBlankName() {
        User user = userController.create(new User(
                "user5@mail.ru",
                "user5Login",
                " ",
                LocalDate.of(2002, 1, 1)));

        assertEquals("user5Login", user.getName());
    }

    @Test
    void createUserWithWrongEmail() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(new User(
                        "user6.mail.ru",
                        "user6Login",
                        "user6Name",
                        LocalDate.of(2003, 1, 1))));

        assertEquals("Ошибка валидации пользователя при создании.", exception.getMessage());
    }

    @Test
    void createUserWithWrongBirthday() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(new User(
                        "user7@mail.ru",
                        "user7Login",
                        "user7Name",
                        LocalDate.of(2100, 1, 1))));

        assertEquals("Ошибка валидации пользователя при создании.", exception.getMessage());
    }

    @Test
    void createUserWithWrongLogin() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(new User(
                        "user8@mail.ru",
                        "user8 Login",
                        "user8Name",
                        LocalDate.of(2000, 1, 1))));

        assertEquals("Ошибка валидации пользователя при создании.", exception.getMessage());
    }
}