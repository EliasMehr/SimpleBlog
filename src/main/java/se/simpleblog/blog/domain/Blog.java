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

    private int likeCount;

    private int dislikeCount;

    @CreationTimestamp
    private LocalDateTime published;

    @JsonBackReference
    @ManyToOne(fetch = EAGER, cascade = ALL)
    private User owner;

    //@JsonIgnoreProperties({"blog", "commentByUser", "owner"})
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


}
