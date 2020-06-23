package se.simpleblog.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.simpleblog.blog.domain.User;
import se.simpleblog.blog.exception.APIRequestException;
import se.simpleblog.blog.repository.UserRepository;

import java.io.FileInputStream;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // Register a new user
    @Transactional
    public void register(User user) {
        isEmailTaken(repository.existsByEmail(user.getEmail()), "Email is already taken!");
        repository.save(user);
    }

    // Fetching all users from database
    public List<User> findAll() {
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(User::getFirstName))
                .collect(Collectors.toList());
    }

    // Update an existing users credentials
    @Transactional
    public void update(UUID userID, User user) {
        repository.findById(userID).ifPresentOrElse(userObject -> {
            user.setId(userObject.getId());
            repository.save(user);
        }, () -> { throw new APIRequestException("Cannot find user"); });
    }

    // Delete an existing user
    @Transactional
    public void delete(UUID userID) {
        repository.findById(userID).ifPresentOrElse(repository::delete,
        () -> { throw new APIRequestException("Cannot delete user by ID: " + userID); });
    }

    // Method to check if an email is already to not allow duplicate emails to register
    public void isEmailTaken(boolean isPresent, String message) {
        if (isPresent) {
            throw new APIRequestException(message);
        }
    }
}
