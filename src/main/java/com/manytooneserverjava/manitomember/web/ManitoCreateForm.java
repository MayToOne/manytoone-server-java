package com.manytooneserverjava.manitomember.web;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public record ManitoCreateForm(String name, LocalDateTime endDateTime) {
}
