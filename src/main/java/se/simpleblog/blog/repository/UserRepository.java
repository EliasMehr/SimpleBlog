package se.simpleblog.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.simpleblog.blog.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(@NotEmpty @Email(message = "Email should be valid") String email);
    boolean existsByEmail(@NotEmpty @Email(message = "Email should be valid") String email);
}
