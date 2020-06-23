package se.simpleblog.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity(name = "Users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String profileImageLink;

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
    @Size(min = 1, max = 100)
    private String password;

    @OneToMany(fetch = EAGER, mappedBy = "owner", cascade = ALL)
    private Set<Blog> blogs = new HashSet<>();

    @JsonIgnoreProperties({"blog"})
    @OneToMany(mappedBy = "commentByUser", fetch = EAGER)
    private Set<Comment> comments = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = EAGER, mappedBy = "likes")
    private Set<Blog> blogLikes = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = EAGER, mappedBy = "disLikes")
    private Set<Blog> blogDisLikes = new HashSet<>();

    public void addBlog(Blog blog) {
        blogs.add(blog);
        blog.setOwner(this);
    }

    public void deleteBlog(Blog blog) {
        blogs.remove(blog);
        blog.setOwner(null);
    }
}
