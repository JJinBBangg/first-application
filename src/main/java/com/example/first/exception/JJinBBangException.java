package com.example.first.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class JJinBBangException extends RuntimeException{

    public final Map<String, String> validation = new HashMap<>();
    public JJinBBangException(String message) {
        super(message);
    }

    public JJinBBangException(String message, Throwable cause) {
        super(message, cause);
    }
    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
