package com.manytooneserverjava.manito.web;

import com.manytooneserverjava.manitomember.domain.ManitoMember;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.ArrayList;

public record ManitoDto(Long id, String name, LocalDateTime endDateTime, Boolean status,
                        String inviteLink, ArrayList<ManitoMember> manitoMembers) {

}
