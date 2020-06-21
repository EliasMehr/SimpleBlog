package se.simpleblog.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.simpleblog.blog.domain.Blog;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlogRepository extends JpaRepository<Blog, UUID> {

    List<Blog> findAllByOwnerId(UUID owner_id);
}
