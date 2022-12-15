package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    @Override
//    public Map<Long, Film> findAll() {
//        return null;
//    }

    @Override
    public List<Film> findAll() {
        return null;
    }

    @Override
    public Film save(Film film) {
//        String sqlQuery = "insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(con -> {
//            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"USER_ID"});
//            stmt.setString(1, user.getEmail());
//            stmt.setString(2, user.getLogin());
//            stmt.setString(3, user.getName());
//            final LocalDate birthday = user.getBirthday();
//            if (birthday == null) {
//                stmt.setNull(4, Types.DATE);
//            } else {
//                stmt.setDate(4, Date.valueOf(birthday));
//            }
//            return stmt;
//        }, keyHolder);
//        user.setId(Objects.requireNonNull(keyHolder.getKey().longValue()));
//        return user;
    return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public void delete(long id) {
    }

    @Override
    public Film findFilmById(long id) {
//        final String sqlQuery = "select USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY" +
//                " from USERS" +
//                " where USER_ID = ?";
//        final List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::makeUser, id);
//        if (users.size() != 1) {
//            return null;
//        }
//        return users.get(0);
        return null;
    }
}
