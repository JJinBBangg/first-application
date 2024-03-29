package com.example.first.exception;

public class PostNotFound extends JJinBBangException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

    public PostNotFound(String message) {
        super(message);
    }
    @Override
    public int getStatusCode() {
        return 404;
    }

    @Override
    public void addValidation(String fieldName, String message) {
        super.addValidation(fieldName, message);
    }
}
