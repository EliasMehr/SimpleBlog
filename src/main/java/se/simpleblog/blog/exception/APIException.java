package se.simpleblog.blog.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class APIException {

    private String message;
    private HttpStatus status;
}
