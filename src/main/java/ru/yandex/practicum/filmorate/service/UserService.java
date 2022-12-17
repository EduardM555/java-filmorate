package ru.yandex.practicum.filmorate.service;

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
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        if (!validate(user)) {
            log.info("Ошибка создания объекта User: {}", user);
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

//    public void addFriend(long userId, long friendId) {
//        User user = userStorage.findAll().get(userId);
//        User friend = userStorage.findAll().get(friendId);
//        if (!userStorage.findAll().containsKey(userId)) {
//            throw new UserNotFoundException("Ошибка валидации пользователя при добавдении в друзья в storage.");
//        }
//        if (!userStorage.findAll().containsKey(friendId)) {
//            throw new FriendNotFoundException("Ошибка валидации друга при добавдении в друзья в storage.");
//        }
//        user.getFriendsIds().add(friend.getId());
//        log.info("В друзья объекта User добавлен объект с friendId {}", friendId);
//        friend.getFriendsIds().add(user.getId());
//        log.info("В друзья объекта User-Friend добавлен объект с userId {}", userId);
//    }

    public void addFriend(long userId, long friendId) {
//        if (getUserById(userId) == null) {
//            throw new UserNotFoundException("Ошибка валидации пользователя при добавдении в друзья," +
//                    " userId=" + userId);
//        }
//        if (getUserById(friendId) == null) {
//            throw new FriendNotFoundException("Ошибка валидации друга при добавдении в друзья, friendId=" +
//                    friendId);
//        }
        getUserById(userId);
        getUserById(friendId);
        userStorage.addFriend(userId, friendId);
    }

//    public void removeFriend(long userId, long friendId) {
//        User user = userStorage.findAll().get(userId);
//        User friend = userStorage.findAll().get(friendId);
//        if (!userStorage.findAll().containsKey(userId)) {
//            throw new UserNotFoundException("Ошибка валидации пользователя при удалении из друзей из storage.");
//        }
//        if (!userStorage.findAll().containsKey(friendId)) {
//            throw new FriendNotFoundException("Ошибка валидации друга при удалении из друзей из storage.");
//        }
//        user.getFriendsIds().remove(friend.getId());
//        log.info("Из друзей объекта User удален объект с friendId {}", friendId);
//        friend.getFriendsIds().remove(user.getId());
//        log.info("Из друзей объекта User-Friend удален объект с userId {}", userId);
//    }
    public void removeFriend(long userId, long friendId) {
        userStorage.removeFriend(userId, friendId);
    }

//    public List<User> getFriends(long userId) {
//        return userStorage.findAll().get(userId).getFriendsIds().stream()
//                .map(id -> userStorage.findAll().get(id))
//                .collect(Collectors.toList());
//    }

    public List<User> getFriends(long userId) {
        getUserById(userId);
        return userStorage.getFriends(userId);
    }

//    public Collection<User> getCommonFriends(long userId, long friendId) {
//        return userStorage.findAll().get(userId).getFriendsIds().stream()
//                .filter(id -> userStorage.findAll().get(friendId).getFriendsIds().contains(id))
//                .map(id -> userStorage.findAll().get(id))
//                .collect(Collectors.toList());
//    }

//    public List<User> getCommonFriends(long userId, long friendId) {
//        return userStorage.getCommonFriends(userId, friendId);
//    }

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
