package com.manytooneserverjava.excpetion.object;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    private final Integer status;
    private final String message;

    public ExceptionResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ExceptionResponse of(Integer status, Exception exception) {
        return new ExceptionResponse(status, exception.getMessage());
    }
}
