package com.manytooneserverjava.manito.web.dto;

import com.manytooneserverjava.manitomember.domain.ManitoMember;

import java.time.LocalDateTime;
import java.util.List;

public record ManitoDto(Long id, String name, LocalDateTime endDateTime, Integer status,
                        String inviteLink, List<ManitoMember> manitoMembers) {

}
