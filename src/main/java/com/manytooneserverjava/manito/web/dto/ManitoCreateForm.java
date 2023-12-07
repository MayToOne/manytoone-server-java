package com.manytooneserverjava.manito.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ManitoCreateForm(
        @JsonProperty("memberId") Long memberId,
        @JsonProperty("name") String name,
        @JsonProperty("endDateTime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime endDateTime) { }
