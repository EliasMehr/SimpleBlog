package se.simpleblog.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.simpleblog.blog.domain.Blog;
import se.simpleblog.blog.domain.Comment;
import se.simpleblog.blog.domain.User;
import se.simpleblog.blog.exception.APIRequestException;
import se.simpleblog.blog.repository.BlogRepository;
import se.simpleblog.blog.repository.CommentRepository;
import se.simpleblog.blog.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BlogService {

    private final BlogRepository repository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public BlogService(BlogRepository repository, UserRepository userRepository, CommentRepository commentRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public List<Blog> findAllBlogs(UUID userID) {
        return repository.findAllByOwnerId(userID).stream()
                .sorted(Comparator.comparing(Blog::getPublished))
                .collect(Collectors.toList());
    }

    @Transactional
    public void create(UUID userID, Blog blog) {
        userRepository.findById(userID).ifPresentOrElse(user -> {
            user.addBlog(blog);
            repository.save(blog);
        }, () -> { throw new APIRequestException("Could not save blog to user"); });
    }

    // TODO ADD to BlogController
    @Transactional
    public void update(UUID blogID, Blog blog) {
        repository.findById(blogID).ifPresentOrElse(blogItem -> {
                    blog.setId(blogItem.getId());
                    repository.save(blog); }, () -> { throw new APIRequestException("Cannot find requested blog item by this ID"); });
    }
    // TODO ADD to BlogController
    @Transactional
    public void delete(UUID blogID) {
        repository.findById(blogID).ifPresentOrElse(repository::delete,
                () -> { throw new APIRequestException("Cannot delete requested blog item"); });
    }

    @Transactional
    public void addComment(UUID blogID, Comment comment, UUID userID) {
        repository.findById(blogID).ifPresentOrElse(blog -> {
            Optional<User> userByID = userRepository.findById(userID);
            blog.addComment(comment, userByID.get());
        }, () -> { throw new APIRequestException("Could not add comment"); });
    }

    @Transactional
    public void deleteComment(UUID blogID, UUID commentID) {
        repository.findById(blogID).ifPresentOrElse(blog -> {
            Optional<Comment> comment = commentRepository.findById(commentID);
            blog.deleteComment(comment.get());
        }, () -> { throw new APIRequestException("Could not delete comment"); });
    }


}
