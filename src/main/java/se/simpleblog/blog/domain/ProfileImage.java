package se.simpleblog.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImage {

    @Id
    @GeneratedValue
    private UUID id;
    
    private String fileName;
    
    private String fileType;
    
    @Lob
    private byte[] image;

    public ProfileImage(String fileName, String contentType, byte[] bytes) {
    }
}
