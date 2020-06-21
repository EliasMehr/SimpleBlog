package se.simpleblog.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class APIExceptionHandler {

    @ExceptionHandler(APIRequestException.class)
    public ResponseEntity<?> exception(APIRequestException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
