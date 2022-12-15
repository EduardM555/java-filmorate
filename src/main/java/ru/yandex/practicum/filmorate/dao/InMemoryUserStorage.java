///*
//package ru.yandex.practicum.filmorate.dao;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
//import ru.yandex.practicum.filmorate.model.User;
//
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Qualifier
//@Slf4j
//@Component
//public class InMemoryUserStorage implements UserStorage {
//
//    private final Map<Long, User> users = new HashMap<>();
//
//    @Override
//    public Map<Long, User> findAll() {
//        return users;
//    }
//
//    @Override
//    public User save(User user) {
//        users.put(user.getId(), user);
//        log.info("Успешное создание объекта: {}", user);
//        return user;
//    }
//
//    @Override
//    public User update(User user) {
//        if (!users.containsKey(user.getId())) {
//            throw new UserNotFoundException("Пользователя: " + user + " в базе нет.");
//        }
//        users.put(user.getId(), user);
//        log.info("Успешное обновление объекта User: {}", user);
//        return user;
//    }
//
//    @Override
//    public User delete(long id) {
//        if (!users.containsKey(id)) {
//            throw new UserNotFoundException("Ошибка валидации пользователя при удалении.");
//        }
//        User deletedUser = users.get(id);
//        users.remove(id);
//        return deletedUser;
//    }
//
//    @Override
//    public User findUserById(long id) {
//        if (!users.containsKey(id)) {
//            throw new UserNotFoundException("Ошибка запроса пользователя с id " + id);
//        }
//        log.info("Следующее действие получение объекта User по id: {}", id);
//        return users.get(id);
//    }
//
//    @Override
//    public void addFriend(long userId, long friendId) {
//
//    }
//}
//*/
