package se.simpleblog.blog.exception;

public class APIRequestException extends RuntimeException {

    public APIRequestException(String message) {
        super(message);
    }
}
