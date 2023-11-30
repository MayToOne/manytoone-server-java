package com.manytooneserverjava.manitomember.web;

import com.manytooneserverjava.manito.domain.Manito;
import com.manytooneserverjava.member.domain.Member;

public record ManitoMemberDto(Long id, Member member, Manito manito, Integer status,
                              String nickname, String wantedGift, String unwantedGift,
                              Boolean isLeader, Member myManito) {
}
