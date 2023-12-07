package com.manytooneserverjava.manitomember.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ManitoMemberStatusForm(@JsonProperty @NotNull Long manitoMemberId, @JsonProperty @Min(0) @Max(1) Integer manitoMemberStatus) {
}
