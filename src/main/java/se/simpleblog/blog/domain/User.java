package se.simpleblog.blog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity(name = "Users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    private UUID id;

//    @Lob
//    @Column(columnDefinition = "BLOB")
//    private byte[] profileImage;

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

    @OneToMany(mappedBy = "commentByUser", fetch = EAGER, cascade = ALL)
    private Set<Comment> comments = new HashSet<>();
}
