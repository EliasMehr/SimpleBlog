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

    @Transactional
    public void delete(UUID userID ,UUID blogID) {
       userRepository.findById(userID).ifPresentOrElse(user -> {
           Optional<Blog> blogByID = repository.findById(blogID);
           user.deleteBlog(blogByID.get());
       }, () -> { throw new APIRequestException("Cannot find blog by id"); });
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


    private void addLike(UUID blogID, UUID userID) {
        repository.findById(blogID).ifPresentOrElse(blog -> {
            Optional<User> userExistsInLikes = blog.getLikes().stream()
                    .filter(user -> userID.equals(user.getId()))
                    .findAny();

            Optional<User> userExistsInDislike = blog.getDisLikes().stream()
                    .filter(user -> userID.equals(user.getId()))
                    .findAny();

                    if (userExistsInLikes.isPresent()) {
                        throw new APIRequestException("You cant like more than once");
                    } else if (userExistsInDislike.isPresent()) {
                        userRepository.findById(userID).ifPresent(user -> {
                            blog.getLikes().add(user);
                            blog.getDisLikes().remove(user);
                        });
                    } }, () -> { throw new APIRequestException("Cannot find blog by id"); });
    }



    private void deleteLike(UUID blogID, UUID userID) {
        repository.findById(blogID).ifPresentOrElse(blog -> {
            blog.getLikes().stream()
                    .filter(user -> userID.equals(user.getId()))
                    .findAny()
                    .ifPresent(blog::deleteLike);

        }, () -> { throw new APIRequestException("No user found in likes by id"); });
    }

    private void addDislike(UUID blogID, UUID userID) {
        repository.findById(blogID).ifPresent(blog -> {
            Optional<User> userExistsInDislike = blog.getDisLikes().stream()
                    .filter(user -> userID.equals(user.getId()))
                    .findAny();

            Optional<User> userExistsInLikes = blog.getLikes().stream()
                    .filter(user -> userID.equals(user.getId()))
                    .findAny();

            if (userExistsInDislike.isPresent()) {
                throw new APIRequestException("You cannot dislike more than once");
            } else if (userExistsInLikes.isPresent()) {
                userRepository.findById(userID).ifPresent(user -> {
                    blog.getLikes().remove(user);
                    blog.getDisLikes().add(user);
                });
            }
        });
    }

    private void deleteDislike(UUID blogID, UUID userID) {
        repository.findById(blogID).ifPresent(blog -> {
            blog.getDisLikes().stream()
                    .filter(user -> userID.equals(user.getId()))
                    .findAny()
                    .ifPresent(blog::deleteDislike);
        });
    }

    @Transactional
    public void handleLikes(UUID blogID, UUID userID, Type status) {
        switch (status) {
            case LIKE -> addLike(blogID, userID);
            case UNLIKE -> deleteLike(blogID, userID);
            case DISLIKE -> addDislike(blogID, userID);
            case UNDISLIKE -> deleteDislike(blogID, userID);
        }
    }

    public enum Type {
        UNLIKE,
        LIKE,
        DISLIKE,
        UNDISLIKE
        }
}
