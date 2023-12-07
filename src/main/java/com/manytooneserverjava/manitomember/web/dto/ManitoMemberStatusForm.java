package com.manytooneserverjava.manitomember.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ManitoMemberStatusForm(@JsonProperty Long manitoMemberId, @JsonProperty Integer manitoMemberStatus) {
}
