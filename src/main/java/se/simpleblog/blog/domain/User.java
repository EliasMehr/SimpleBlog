package se.simpleblog.blog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] profileImage;

    @NotBlank
    @Size(min = 2, max = 20, message = "first-name must contain min 2 characters and max 20 characters")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 20, message = "last-name must contain min 2 characters and max 20 characters")
    private String lastName;

    @Digits(integer = 2, fraction = 0)
    @Positive(message = "Age cannot be 0 or negative value")
    private int age;

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Size(min = 1,max = 100)
    private String password;

    @OneToMany(mappedBy = "owner")
    Set<Blog> blogs = new HashSet<>();

    @OneToMany(mappedBy = "commentBy")
    Set<Comment> comments = new HashSet<>();

    public void addBlog(Blog blog) {
        blogs.add(blog);
        blog.setOwner(this);
    }

    public void deleteBlog(Blog blog) {
        blogs.remove(blog);
        blog.setOwner(null);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setCommentBy(this);
        // TODO fix so that comments match for the current blog-post
    }
}
