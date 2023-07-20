package com.example.first.exception;

public class RegionNotFound extends JJinBBangException {

    private static final String MESSAGE = "지역을 올바르게 입력하세요.";

    public RegionNotFound() {
        super(MESSAGE);
    }

    //    public PostNotFound(Throwable cause) {
//        super(MESSAGE, cause);
//    }

    public RegionNotFound(String message) {
        super(message);
    }


    @Override
    public int getStatusCode() {
        return 404;
    }
}
