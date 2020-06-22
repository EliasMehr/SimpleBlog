package se.simpleblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.simpleblog.blog.domain.Blog;
import se.simpleblog.blog.domain.Comment;
import se.simpleblog.blog.exception.APIRequestException;
import se.simpleblog.blog.service.BlogService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/blog")
public class BlogController {

    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/{user}")
    public ResponseEntity<List<Blog>> getAllBlogs(@PathVariable UUID user) {
        try {
            return ResponseEntity.ok(blogService.findAllBlogs(user));
        } catch (APIRequestException exception) {
            throw new APIRequestException(exception.getMessage());
        }
    }

    @PostMapping("/{user}")
    public void create(@PathVariable UUID user, @RequestBody Blog blog) {
        blogService.create(user, blog);
    }

    @PatchMapping("/{blogID}/{userID}")
    public ResponseEntity<String> addComment(@PathVariable UUID blogID, @PathVariable UUID userID, @RequestBody Comment comment) {
        blogService.addComment(blogID, comment, userID);
        return ResponseEntity.ok("Successfully added a comment");
    }

    @DeleteMapping("/{blogID}/{commentID}")
    public ResponseEntity<String> deleteComment(@PathVariable UUID blogID, @PathVariable UUID commentID) {
        blogService.deleteComment(blogID, commentID);
        return ResponseEntity.ok("Deleted comment successfully");
    }
}
