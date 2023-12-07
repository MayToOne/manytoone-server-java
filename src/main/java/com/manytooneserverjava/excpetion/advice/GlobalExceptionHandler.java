package com.manytooneserverjava.excpetion.advice;

import com.manytooneserverjava.excpetion.object.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> illegalStateExceptionHandler(IllegalStateException e) {
        System.out.println("BAD_REQUEST.getReasonPhrase() = " + BAD_REQUEST.getReasonPhrase());
        return ResponseEntity.badRequest().body(ExceptionResponse.of(BAD_REQUEST.value(), e));
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> noSuchElementExceptionHandler(NoSuchElementException e) {
        return ResponseEntity.badRequest().body(ExceptionResponse.of(BAD_REQUEST.value(), e));
    }

}
