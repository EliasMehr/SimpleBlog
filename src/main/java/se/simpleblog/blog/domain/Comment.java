package se.simpleblog.blog.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    private UUID id;

    private Date published;

    private String comment;

    @ManyToOne
    private Blog commentBy;

    @ManyToOne(fetch = EAGER, cascade = ALL)
    private User commentByUser;

}
