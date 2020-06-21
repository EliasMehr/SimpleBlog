package se.simpleblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.simpleblog.blog.domain.User;
import se.simpleblog.blog.exception.APIRequestException;
import se.simpleblog.blog.service.UserService;

import javax.validation.Valid;
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
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        try {
            userService.register(user);
            return ResponseEntity.ok(user);
        } catch (APIRequestException ex) {
            throw new APIRequestException("Failure");
        }
    }

}
