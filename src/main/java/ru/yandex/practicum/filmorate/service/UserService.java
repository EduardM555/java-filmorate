package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserService {
    public final UserStorage userStorage;
    private long generatedId = 0;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.getAll().values();
    }

    public User create(User user) {
        if (!validate(user)) {
            log.info("Ошибка создания объекта User: {}", user);
            throw new ValidationException("Ошибка валидации пользователя при создании.");
        }
        user.setId(generateId());
        return userStorage.create(user);
    }

    public User update(User user) {
        if (!validate(user)) {
            log.info("Ошибка обновления объекта User: {}", user);
            throw new ValidationException("Ошибка валидации пользователя при обнолвении.");
        }
        return userStorage.update(user);
    }

    public User delete(long id) {
        return userStorage.delete(id);
    }

    public User getUserById(long userId) {
//        User user = userStorage.getById(userId);
//        if (!validate(user)) {
//            log.info("Ошибка получения объекта User: {}", user);
//            throw new ValidationException("Ошибка валидации пользователя при получении.");
//        }
//        if (user == null) {
//            throw new UserNotFoundException("Ошибка запроса пользователя с id " + userId);
//        }
        return userStorage.getById(userId);
    }

    public void addFriend(long userId, long friendId) {
        User user = userStorage.getAll().get(userId);
        User friend = userStorage.getAll().get(friendId);
//        if (!validate(user)) {
//            throw new ValidationException("Ошибка валидации пользователя при добавдении в друзья.");
//        }
//        if (!validate(friend)) {
//            throw new ValidationException("Ошибка валидации друга при добавдении в друзья.");
//        }
        if (!userStorage.getAll().containsKey(userId)) {
            throw new UserNotFoundException("Ошибка валидации пользователя при добавдении в друзья в storage.");
        }
        if (!userStorage.getAll().containsKey(friendId)) {
            throw new FriendNotFoundException("Ошибка валидации друга при добавдении в друзья в storage.");
        }
        user.getFriendsIds().add(friend.getId());
        friend.getFriendsIds().add(user.getId());
    }

    public void removeFriend(long userId, long friendId) {
        User user = userStorage.getAll().get(userId);
        User friend = userStorage.getAll().get(friendId);
        if (!userStorage.getAll().containsKey(userId)) {
            throw new UserNotFoundException("Ошибка валидации пользователя при удалении из друзей из storage.");
        }
        if (!userStorage.getAll().containsKey(friendId)) {
            throw new FriendNotFoundException("Ошибка валидации друга при удалении из друзей из storage.");
        }
        user.getFriendsIds().remove(friend.getId());
        friend.getFriendsIds().remove(user.getId());
    }

    public List<User> getFriends(long userId) {
        return userStorage.getAll().get(userId).getFriendsIds().stream()
                .map(id -> userStorage.getAll().get(id))
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(long userId, long friendId) {
        return userStorage.getAll().get(userId).getFriendsIds().stream()
                .filter(id -> userStorage.getAll().get(friendId).getFriendsIds().contains(id))
                .map(id -> userStorage.getAll().get(id))
                .collect(Collectors.toList());
    }

    private long generateId() {
        ++generatedId;
        log.info("Объекту класса User присовен id: {}", generatedId);
        return generatedId;
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
