package com.example.krisinoteBackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int save(User user) {
        String sql = "INSERT INTO users (firstName, lastName, email, password, role) VALUES (?, ?, ?, ?, CAST(? AS CHAR(10)))";
        System.out.println(user.getRole());
        jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getRole().name());
        return 0;
    }

    @Override
    public int update(User user, int id) {
        String sql = "UPDATE users SET firstName = ?, lastName = ?, email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), id);
        return 0;
    }


    @Override
    public int delete(int id) {
        return 0;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users", new BeanPropertyRowMapper<User>(User.class));
    }

    @Override
    public User getUserById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id=?", new BeanPropertyRowMapper<User>(User.class), id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", new BeanPropertyRowMapper<User>(User.class), email);
        if (users.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(users.get(0));
        }
    }

}
