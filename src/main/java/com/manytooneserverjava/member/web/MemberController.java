package com.manytooneserverjava.member.web;

import com.manytooneserverjava.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/member")
public class MemberController {
    private final MemberService memberService;
    @PostMapping
    public ResponseEntity<Long> createMember(@RequestBody MemberCreateForm form) {
        Long memberId = memberService.createMember(form);
        return ResponseEntity.ok(memberId);
    }
}
