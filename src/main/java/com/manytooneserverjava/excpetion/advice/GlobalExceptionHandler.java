package com.manytooneserverjava.excpetion.advice;

import com.manytooneserverjava.excpetion.object.ExceptionResponse;
import com.manytooneserverjava.excpetion.object.ExceptionResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> illegalStateExceptionHandler(IllegalStateException e) {
        return ResponseEntity.badRequest().body(ExceptionResponse.of(BAD_REQUEST.value(), e));
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> noSuchElementExceptionHandler(NoSuchElementException e) {
        return ResponseEntity.badRequest().body(ExceptionResponse.of(BAD_REQUEST.value(), e));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponses> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> errorMessages = fieldErrors.stream().map(fieldError -> fieldError.getField() + ":" + fieldError.getDefaultMessage()).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new ExceptionResponses(BAD_REQUEST.value(), errorMessages));
    }
}
