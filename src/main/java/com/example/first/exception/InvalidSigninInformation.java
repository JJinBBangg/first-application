package com.example.first.exception;

public class InvalidSigninInformation extends JJinBBangException {

    private static final String MESSAGE = "아이디 또는 비밀번호가 올바르지 않습니다.";
    public InvalidSigninInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
