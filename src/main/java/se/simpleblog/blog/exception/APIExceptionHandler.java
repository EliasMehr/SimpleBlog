package se.simpleblog.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class APIExceptionHandler {

    @ExceptionHandler(APIRequestException.class)
    public ResponseEntity<?> exception(APIRequestException exception) {
        APIException apiException = new APIException(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}
