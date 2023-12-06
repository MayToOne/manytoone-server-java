package com.manytooneserverjava.manitomember.service;

import com.manytooneserverjava.manito.domain.Manito;
import com.manytooneserverjava.manito.domain.ManitoRepository;
import com.manytooneserverjava.manito.web.ManitoDto;
import com.manytooneserverjava.manitomember.domain.ManitoMember;
import com.manytooneserverjava.manitomember.domain.ManitoMemberRepository;
import com.manytooneserverjava.manitomember.web.GiftInfoForm;
import com.manytooneserverjava.manitomember.web.ManitoMemberDto;
import com.manytooneserverjava.member.domain.Member;
import com.manytooneserverjava.member.domain.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManitoMemberService {
    private final MemberRepository memberRepository;
    private final ManitoRepository manitoRepository;
    private final ManitoMemberRepository manitoMemberRepository;

    /**
     * 마니또 그룹 초대 생성
     * @param manitoId
     * @param memberId
     */
    @Transactional
    public Long inviteManitoMember(Long manitoId, Long memberId) {
        Member findMember = memberRepository.findById(memberId).get();
        Manito findManito = manitoRepository.findById(manitoId).get();
        ManitoMember manitoMember = ManitoMember.builder()
                .manito(findManito)
                .member(findMember)
                .status(0)
                .isLeader(false)
                .build();
        return manitoMemberRepository.save(manitoMember).getId();
    }

    /**
     * 마니또 그룹 초대 승인/거절 매서드
     * @param manitoMemberId
     * @param status
     */
    @Transactional
    public void updateManitoMemberStatus(Long manitoMemberId, Integer status) {
            ManitoMember findManitoMember = manitoMemberRepository.findById(manitoMemberId).get();
        if (status == 1) {
            findManitoMember.editInviteStatus(status);
        } else {
            manitoMemberRepository.delete(findManitoMember);
        }
    }


    /**
     * 마니또 그룹 초대된 내역 조회 매서드
     * @param memberId
     * @return
     */
    @Transactional
    public List<ManitoMemberDto> findMyManitoInvites(Long memberId) {
        List<ManitoMember> findManitoMembers = manitoMemberRepository.findMyManitoInvites(memberId);
        List<ManitoMemberDto> result = new ArrayList<>();
        for (ManitoMember fmm : findManitoMembers) {
            result.add(new ManitoMemberDto(fmm.getId(), fmm.getMember(), fmm.getManito(),
                    fmm.getStatus(), fmm.getNickname(), fmm.getWantedGift(),
                    fmm.getUnwantedGift(), fmm.getIsLeader(), fmm.getMyManito()));
        }
        return result;
    }

    /**
     * 초대 후 원하거나 원하지 않는 선물 정보를 입력하는 매서드
     * @param form
     */
    @Transactional
    public void saveGiftInfo(GiftInfoForm form) {
        ManitoMember findManitoMember = manitoMemberRepository.findById(form.manitoMemberId()).get();
        findManitoMember.editGiftInfo(form.wantedGift(), form.unwantedGift());
    }

    public List<ManitoDto> findMyManitos(Long memberId) {
        List<Manito> myManitos = manitoMemberRepository.findMyManito(memberId);
        List<ManitoDto> findManitos = new ArrayList<>();
        for (Manito manito : myManitos) {
            findManitos.add(new ManitoDto(manito.getId(), manito.getName(), manito.getEndDateTime(),
                    manito.getStatus(), manito.getInviteLink(), (ArrayList<ManitoMember>) manito.getManitoMembers()));
        }
        return findManitos;
    }


}
