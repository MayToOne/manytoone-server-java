package com.manytooneserverjava.manitomember.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record GiftInfoForm(@JsonProperty @NotNull Long manitoMemberId, @JsonProperty String wantedGift, @JsonProperty String unwantedGift) {
}
