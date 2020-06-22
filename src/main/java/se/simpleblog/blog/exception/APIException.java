package se.simpleblog.blog.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
public class APIException {

    private final String message;
    private final HttpStatus status;
}
