package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
@Slf4j
//@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        String sqlQuery = "select USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY from USERS";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser);
    }


//    public Map<Long, User> findAll() {
//        Map<Long, User> users = new HashMap<>();
//        String sqlQuery = "select USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY from USERS";
//        jdbcTemplate.queryForStream(sqlQuery, UserDbStorage::makeUser)
//                .collect(Collectors.toList())
////                .map(user -> users.put(user.getId(), user))
//                .forEach(user -> users.put(user.getId(), user));
//        return users;
//    }

    @Override
    public User save(User user) {
        String sqlQuery = "insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update USERS set EMAIL = ?, LOGIN = ?, "
                + "USER_NAME = ?, BIRTHDAY = ? where USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void addFriend(long userId, long friendId) {
//        String sqlQuery = "insert into FRIENDSHIP (FRIEND_ID, USER_ID) "
        String sqlQuery = "insert into FRIENDSHIP (USER_ID, FRIEND_ID) "
                + "VALUES (?, ?)";
        log.info("Начало выпонление метода addFriend(long userId, long friendId), {}, {}", userId, friendId);
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("метод addFriend выполнен, {}, {}", userId, friendId);
    }

    @Override
    public List<User> getFriends(long userId) {
//        String sqlQuery = "select FRIEND_ID from FRIENDSHIP where USER_ID = ?";
        String sqlQuery = "select u.USER_ID, u.EMAIL, u.LOGIN, u.USER_NAME, u.BIRTHDAY from USERS AS u" +
                " join FRIENDSHIP AS f on u.USER_ID = f.FRIEND_ID" +
                " where f.USER_ID = ?";
        log.info("Начало выполнение метода List<User> getFriends(long userId) = {}", userId);
        List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId);
        log.info("Окончание выполнение метода List<User> getFriends(long userId) = {}, size()={}", userId, users.size());
        return users;
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        String newSqlQuery = "select * from USERS AS us" +
                " where us.USER_ID in (select fr1.FRIEND_ID from FRIENDSHIP AS fr1" +
//                " join (select fr1.FRIEND_ID from FRIENDSHIP AS fr1" +
                " join FRIENDSHIP AS fr2" +
                " where fr1.USER_ID = ? and fr2.USER_ID = ?)";
//        return jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, userId, friendId);
        return jdbcTemplate.query(newSqlQuery, UserDbStorage::makeUser, userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        String sqlQuery = "delete from FRIENDSHIP where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
//        jdbcTemplate.update(sqlQuery, friendId, userId);
    }

    @Override
    public User findUserById(long id) {
        final String sqlQuery = "select USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY"
                + " from USERS"
                + " where USER_ID = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id);
        if (users.size() != 1) {
            return null;
        }
        return users.get(0);
//        User user = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id).get(0);
//        return Objects.requireNonNullElse(user, null);
    }

    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getLong("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate());
    }
}
