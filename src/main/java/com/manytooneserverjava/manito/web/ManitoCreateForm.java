package com.manytooneserverjava.manito.web;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public record ManitoCreateForm(Long memberId, String name, LocalDateTime endDateTime) {
}
