package com.example.first.exception;

public class DuplicateEmail extends JJinBBangException {

    private static final String MESSAGE = "중복된 e-mail입니다.";
    private  String fieldName;
    private  String message;

    public DuplicateEmail() {
        super(MESSAGE);
    }

    public DuplicateEmail(String message) {
        super(message);
    }


    @Override
    public int getStatusCode() {
        return 409;
    }
}
