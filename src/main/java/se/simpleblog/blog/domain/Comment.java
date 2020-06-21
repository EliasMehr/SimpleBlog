package se.simpleblog.blog.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    private UUID id;

    @CreatedDate
    private Date published;

    private String comment;

    @ManyToOne
    private User commentBy;

    @ManyToOne
    private Blog blog;
}
