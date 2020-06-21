package se.simpleblog.blog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @ManyToOne(fetch = EAGER, cascade = ALL)
    private User owner;

    @OneToMany(fetch = EAGER, mappedBy = "commentBy", cascade = ALL)
    private Set<Comment> comments = new HashSet<>();

}
