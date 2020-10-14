package org.example.app.exceptions;

import org.springframework.ui.Model;

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
