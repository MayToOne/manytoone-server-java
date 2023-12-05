package com.manytooneserverjava.manitomember.web;

import com.manytooneserverjava.manitomember.domain.ManitoMemberRepository;
import com.manytooneserverjava.manitomember.service.ManitoMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/manitomember")
@RequiredArgsConstructor
public class ManitoMemberController {
    private final ManitoMemberService manitoMemberService;
    @GetMapping("/{memberId}")
    public ResponseEntity<List<ManitoMemberDto>> findMyManitoInvites(@PathVariable(name = "memberId") Long memberId) {
        List<ManitoMemberDto> myManitoInvites = manitoMemberService.findMyManitoInvites(memberId);
        return ResponseEntity.ok(myManitoInvites);
    }
}
