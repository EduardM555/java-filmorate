package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private Map<Integer, User> users = new HashMap<>();
    private int generatedId = 0;

    public Map<Integer, User> getUsers() {
        return users;
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (!validate(user)) {
                log.info("Ошибка создания объекта User: {}", user);
                throw new ValidationException("Ошибка валидации пользователя при создании.");
            }
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Успешное создание объекта: {}", user);
        return user;
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователя: " + user + " в базе нет.");
        }
        if (!validate(user)) {
            log.info("Ошибка обнолвения объекта User: {}", user);
            throw new ValidationException("Ошибка валидации пользователя при обнолвении.");
        }
        log.info("Успешное обновление объекта User: {}", user);
        users.put(user.getId(), user);
        return user;
    }

    boolean validate(User user) {
        if (user.getEmail() == null
                || user.getEmail().isBlank()
                || !user.getEmail().contains("@")) {
            return false;
        }
        if (user.getLogin() == null
                || user.getLogin().isBlank()
                || user.getLogin().contains(" ")) {
            return false;
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) return false;
        return true;
    }

    private int generateId() {
        ++generatedId;
        log.info("Объекту класса User присовен id: {}", generatedId);
        return generatedId;
    }
}
