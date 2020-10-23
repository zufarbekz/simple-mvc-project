package org.example.app.exceptions;

public class UploadingException extends Exception {
    String message;

    public UploadingException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
