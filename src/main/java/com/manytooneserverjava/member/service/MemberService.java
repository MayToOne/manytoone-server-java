package com.manytooneserverjava.member.service;

import com.manytooneserverjava.member.domain.Member;
import com.manytooneserverjava.member.domain.MemberRepository;
import com.manytooneserverjava.member.web.MemberCreateForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long createMember(MemberCreateForm form) {
        Member member = Member.builder()
                .email(form.email())
                .passwd(form.passwd())
                .name(form.name())
                .build();
        Member savedMember = memberRepository.save(member);
        return member.getId();
    }
}
