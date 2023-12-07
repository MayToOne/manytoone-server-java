package com.manytooneserverjava.manitomember.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GiftInfoForm(@JsonProperty Long manitoMemberId, @JsonProperty String wantedGift, @JsonProperty String unwantedGift) {
}
