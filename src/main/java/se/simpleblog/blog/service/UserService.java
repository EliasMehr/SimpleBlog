package se.simpleblog.blog.service;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.simpleblog.blog.domain.User;
import se.simpleblog.blog.exception.APIRequestException;
import se.simpleblog.blog.repository.UserRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.http.entity.ContentType.*;

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
    public void update(UUID userID,
                       User user) {

        repository.findById(userID).ifPresentOrElse(userObject -> {
            user.setId(userObject.getId());
            repository.save(user);
        }, () -> {
            throw new APIRequestException("Cannot find user");
        });
    }

    // Delete an existing user
    @Transactional
    public void delete(UUID userID) {
        repository.findById(userID).ifPresentOrElse(repository::delete,
                () -> {
                    throw new APIRequestException("Cannot delete user by ID: " + userID);
                });
    }

    public void upload(UUID userID, MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload an empty file");
        }
        if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an Image of type JPEG, PNG, GIF");
        }

        // Step 3 - Collect metadata from the file
        // --Set these attributes to the ProfileImage fields
        String filename = file.getName();
        String contentType = file.getContentType();

        // Step 4 - Collect user from db and save the byte array
        repository.findById(userID).ifPresent(user -> {
            try {
                user.setImage(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                repository.save(user);
            }
        });

    }

    // Method to check if an email is already to not allow duplicate emails to register
    public void isEmailTaken(boolean isPresent,
                             String message) {

        if (isPresent) {
            throw new APIRequestException(message);
        }
    }
}
