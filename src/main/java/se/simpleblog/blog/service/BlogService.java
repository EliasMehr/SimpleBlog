package se.simpleblog.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.simpleblog.blog.domain.Blog;
import se.simpleblog.blog.exception.APIRequestException;
import se.simpleblog.blog.repository.BlogRepository;

import java.util.UUID;

@Service
public class BlogService {

    private final BlogRepository repository;

    @Autowired
    public BlogService(BlogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void create(Blog blog) {
        repository.save(blog);
    }

    @Transactional
    public void update(UUID blogID, Blog blog) {
        repository.findById(blogID).ifPresentOrElse(blogItem -> {
            blog.setId(blogItem.getId());
            repository.save(blog);
                },
        () -> { throw new APIRequestException("Cannot find requested blog item by this ID"); });


    }

    @Transactional
    public void delete(UUID blogID) {
        repository.findById(blogID).ifPresentOrElse(repository::delete,
        () -> { throw new APIRequestException("Cannot delete requested blog item"); });
    }


}
