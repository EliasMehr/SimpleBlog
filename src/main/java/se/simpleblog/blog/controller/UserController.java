package se.simpleblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import se.simpleblog.blog.domain.User;
import se.simpleblog.blog.exception.APIException;
import se.simpleblog.blog.exception.APIRequestException;
import se.simpleblog.blog.service.UserService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        } catch (APIRequestException exception) {
            throw new ValidationException(exception.getMessage());
        }
    }

    @DeleteMapping("{userID}")
    public ResponseEntity<String> delete(@PathVariable UUID userID) {
        try {
            userService.delete(userID);
            return ResponseEntity.ok("Deleted");
        } catch (APIRequestException exception) {
            throw new APIRequestException(exception.getMessage());
        }
    }

    @PutMapping("{userID}")
    public ResponseEntity<User> update(@PathVariable UUID userID, @Valid @RequestBody User user) {
        userService.update(userID, user);
        return ResponseEntity.ok(user);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
