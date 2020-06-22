package se.simpleblog.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.simpleblog.blog.domain.User;
import se.simpleblog.blog.exception.APIRequestException;
import se.simpleblog.blog.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void register(User user) {
        isEmailTaken(repository.existsByEmail(user.getEmail()), "Email is already taken!");
        repository.save(user);
    }

    public List<User> findAll() {
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(User::getFirstName))
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(UUID userID, User user) {
        repository.findById(userID).ifPresentOrElse(userObject -> {
            user.setId(userObject.getId());
            repository.save(user);
        }, () -> { throw new APIRequestException("Cannot find user"); });
    }

    @Transactional
    public void delete(UUID userID) {
        repository.findById(userID).ifPresentOrElse(repository::delete, () -> { throw new APIRequestException("Cannot delete user by ID: " + userID); });
    }

    public void isEmailTaken(boolean isPresent, String message) {
        if (isPresent) {
            throw new APIRequestException(message);
        }
    }
}
