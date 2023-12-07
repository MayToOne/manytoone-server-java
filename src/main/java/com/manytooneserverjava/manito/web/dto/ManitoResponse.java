package com.manytooneserverjava.manito.web.dto;

public record ManitoResponse<T>(Integer status, String message, T data) {
}
