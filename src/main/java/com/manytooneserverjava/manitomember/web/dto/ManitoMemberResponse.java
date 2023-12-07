package com.manytooneserverjava.manitomember.web.dto;

public record ManitoMemberResponse<T>(Integer status, String message, T data) {
}
