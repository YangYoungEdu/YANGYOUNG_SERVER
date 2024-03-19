package com.yangyoung.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.yangyoung.server")
public class ExceptionManager {

    @ExceptionHandler(MyException.class)
    public ResponseEntity<?> myExceptionHandler(MyException e) {
        e.printStackTrace();
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(new ErrorCode[]{e.getErrorCode()});
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String runtimeExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        return "Runtime Exception 발생!";
    }
}
