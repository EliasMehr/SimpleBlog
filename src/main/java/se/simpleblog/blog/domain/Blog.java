package se.simpleblog.blog.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Blog {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private String context;

    @CreationTimestamp
    private LocalDateTime published;

    @JsonBackReference
    @ManyToOne(fetch = EAGER)
    private User owner;

    @JsonIgnoreProperties({"age", "email", "password", "blogs", "comments"})
    @ManyToMany(fetch = EAGER, cascade = ALL)
    @JoinTable(name = "blog_likes",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id") )
    private Set<User> likes = new HashSet<>();

    @JsonIgnoreProperties({"age", "email", "password", "blogs", "comments"})
    @ManyToMany(fetch = EAGER, cascade = ALL)
    @JoinTable(name = "blog_dislikes",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id") )
    private Set<User> disLikes = new HashSet<>();

    @OneToMany(fetch = EAGER, mappedBy = "blog", cascade = ALL)
    private Set<Comment> comments = new HashSet<>();

    public void addComment(Comment comment, User user) {
        comments.add(comment);
        comment.setBlog(this);
        comment.setCommentByUser(user);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
        comment.setBlog(null);
        comment.setCommentByUser(null);
    }

    public void addLike(User user) {
        likes.add(user);
        user.getBlogLikes().add(this);
    }

    public void deleteLike(User user) {
        likes.remove(user);
        user.getBlogLikes().remove(this);
    }

    public void addDislike(User user) {
        disLikes.add(user);
        user.getBlogDisLikes().add(this);
    }

    public void deleteDislike(User user) {
        disLikes.remove(user);
        user.getBlogDisLikes().remove(this);
    }


}
