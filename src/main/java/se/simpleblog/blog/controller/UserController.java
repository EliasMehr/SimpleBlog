package se.simpleblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.simpleblog.blog.domain.User;
import se.simpleblog.blog.exception.APIRequestException;
import se.simpleblog.blog.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.findAll());
        } catch (APIRequestException exception) {
            throw new APIRequestException("Users unavailable");
        }
    }
}
