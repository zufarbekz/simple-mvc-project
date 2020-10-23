package org.example.app.exceptions;


public class BooksShelfLoginException extends Exception {

    private String message;

    public BooksShelfLoginException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
