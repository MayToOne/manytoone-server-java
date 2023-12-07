package com.manytooneserverjava.manitomember.web;

import com.manytooneserverjava.manitomember.service.ManitoMemberService;
import com.manytooneserverjava.manitomember.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.manytooneserverjava.common.message.ResponseMessage.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/manitoMembers")
@RequiredArgsConstructor
public class ManitoMemberController {
    private final ManitoMemberService manitoMemberService;

    @PostMapping()
    public ResponseEntity<ManitoMemberResponse<Long>> inviteManitoMember(@RequestBody @Valid ManitoInviteForm form) {
        ManitoMemberResponse<Long> response = new ManitoMemberResponse<>(OK.value(), MANITO_MEMBER_INVITE_SUECCESS,
                manitoMemberService.inviteManitoMember(form.manitoId(), form.memberId()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<ManitoMemberResponse<?>> updateInviteStatus(@RequestBody @Valid ManitoMemberStatusForm form) {
        ManitoMemberResponse<Object> response = new ManitoMemberResponse<>(OK.value(), MANITO_MEMBER_INVITE_UPDATE_SUCCESS, null);
        manitoMemberService.updateManitoMemberStatus(form.manitoMemberId(), form.manitoMemberStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ManitoMemberResponse<List<ManitoMemberDto>>> findMyManitoInvites(@PathVariable(name = "memberId") Long memberId) {
        ManitoMemberResponse<List<ManitoMemberDto>> response = new ManitoMemberResponse<>(OK.value(), MANITO_MEMBER_SELECT_SUECCESS,
                manitoMemberService.findMyManitoInvites(memberId));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateGiftInfo")
    public ResponseEntity<ManitoMemberResponse<?>> updateGiftInfo(@RequestBody @Valid GiftInfoForm form) {
        manitoMemberService.saveGiftInfo(form);
        ManitoMemberResponse<Object> response = new ManitoMemberResponse<>(OK.value(), MANITO_MEMBER_UPDATE_GIFT_SUECCESS, null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{manitoMemberId}")
    public ResponseEntity<ManitoMemberResponse<?>> leaveManito(@PathVariable(name = "manitoMemberId") Long manitoMemberId) {
        manitoMemberService.leaveManito(manitoMemberId);
        ManitoMemberResponse<Object> response = new ManitoMemberResponse<>(OK.value(), MANITO_MEMBER_LEAVE_SUECCESS, null);
        return ResponseEntity.ok(response);
    }
}
