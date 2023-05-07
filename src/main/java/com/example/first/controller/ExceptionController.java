package com.example.first.controller;

import com.example.first.exception.JJinBBangException;
import com.example.first.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
//        MethodArgumentNotValidException
//      log.error("exceptionHandler error : ", e);
//        FieldError fieldError =  e.getFieldError();
//        String field = fieldError.getField();
//        String message = fieldError.getDefaultMessage();
//        Map<String, String>  response =new HashMap<>();
//        response.put(field, message);
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();
//        for (FieldError fieldError : e.getFieldErrors()) {
//            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
//        }

//            new ErrorResponse("400", "잘못된 요청입니다.");
        return response;
    }

    @ResponseBody
    @ExceptionHandler(JJinBBangException.class)
    public ResponseEntity<ErrorResponse> JJinBBangExceptionHandler(JJinBBangException e) {
//        if(e instanceof InvalidRequest){
//            //400
//        } else if(e instanceof PostNotFound) {
//            //404
//        }
        int statusCode = e.getStatusCode();
        ErrorResponse response = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();
//        if(e instanceof InvalidRequest){
//            InvalidRequest invalidRequest = (InvalidRequest) e;
//            response.addValidation(invalidRequest.getFieldName(),invalidRequest.getMessage());
//        }

        return ResponseEntity.status(statusCode)
                .body(response);
    }

}
