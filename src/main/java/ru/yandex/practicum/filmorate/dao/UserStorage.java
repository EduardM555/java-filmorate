package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface UserStorage {
//    Map<Long, User> findAll();
    List<User> findAll();
    User save(User user);
    User update(User user);
    void delete(long id);
    User findUserById(long id);
    void addFriend(long userId, long friendId);
    List<User> getFriends(long userId);
    List<User> getCommonFriends(long userId, long friendId);
    void removeFriend(long userId, long friendId);
}
