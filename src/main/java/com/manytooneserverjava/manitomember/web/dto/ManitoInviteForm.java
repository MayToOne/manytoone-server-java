package com.manytooneserverjava.manitomember.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record ManitoInviteForm(@JsonProperty @NotNull Long manitoId, @JsonProperty @NotNull Long memberId) {
}
