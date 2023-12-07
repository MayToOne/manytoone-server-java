package com.manytooneserverjava.manito.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ManitoCreateForm(
        @JsonProperty("memberId") @NotNull Long memberId,
        @JsonProperty("name") @NotBlank String name,
        @JsonProperty("endDateTime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        @NotNull
        LocalDateTime endDateTime) {
}
