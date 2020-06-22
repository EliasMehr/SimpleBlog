package se.simpleblog.blog.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.FetchType.EAGER;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    private LocalDateTime published;

    private String comment;

    @JsonBackReference
    @ManyToOne(fetch = EAGER)
    private Blog blog;

    @JsonIgnoreProperties({"comments", "age", "email", "password", "blogs"})
    @ManyToOne(fetch = EAGER)
    private User commentByUser;

}
