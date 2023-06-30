package com.example.krisinoteBackend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.Integer.parseInt;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping
    public List<User> getUsers() {
        return userDAO.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        System.out.println(id);
        return userDAO.getUserById(parseInt(id));
    }
}
