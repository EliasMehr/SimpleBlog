package se.simpleblog.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.simpleblog.blog.domain.ProfileImage;

import java.util.UUID;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage, UUID> {
}
