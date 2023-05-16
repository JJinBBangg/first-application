package com.example.first.exception;

public class Unauthorized extends JJinBBangException {

    private static final String MESSAGE = "인증이 필요합니다.";

    public Unauthorized() {
        super(MESSAGE);
    }
    public Unauthorized(String message){
        super(message);
    }
    @Override
    public int getStatusCode() {
        return 401;
    }
}
