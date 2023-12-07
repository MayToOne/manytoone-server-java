package com.manytooneserverjava.manito.web.dto;

import java.time.LocalDateTime;

public record ManitoUpdateForm(Integer status, LocalDateTime endDatetime) {
}
