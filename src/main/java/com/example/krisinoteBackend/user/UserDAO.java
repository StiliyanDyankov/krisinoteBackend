package com.example.krisinoteBackend.user;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    int save(User user);

    int update(User user, int id);
    int delete(int id);

    List<User> getAll();

    User getUserById(int id);

    Optional<User> getUserByEmail(String email);
}
