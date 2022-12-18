package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        if (!validate(user)) {
            log.warn("Ошибка создания объекта User: {}", user);
            throw new ValidationException("Ошибка валидации пользователя при создании.");
        }
        log.info("Объекту класса User присовен id: {}", user.getId());
        return userStorage.save(user);
    }

    public User update(User user) {
        User newUser = userStorage.findUserById(user.getId());
        if (newUser == null) {
            throw new UserNotFoundException("Ошибка валидации пользователя при обновлении," +
                    " userId=" + user.getId());
        }
        return userStorage.update(user);
    }

    public void delete(long id) {
        userStorage.delete(id);
    }

    public User getUserById(long userId) {
        User user = userStorage.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("Ошибка валидации пользователя/друга по id=" + userId);
        }
        return user;
    }

    public void addFriend(long userId, long friendId) {
        getUserById(userId);
        getUserById(friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        userStorage.removeFriend(userId, friendId);
    }


    public List<User> getFriends(long userId) {
        getUserById(userId);
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        return getFriends(userId).stream()
                .filter(user -> getFriends(friendId).contains(user))
                .collect(Collectors.toList());
    }

    private boolean validate(User user) {
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
}
