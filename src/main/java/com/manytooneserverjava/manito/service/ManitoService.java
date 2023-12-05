package com.manytooneserverjava.manito.service;

import com.manytooneserverjava.manito.domain.Manito;
import com.manytooneserverjava.manito.domain.ManitoRepository;
import com.manytooneserverjava.manitomember.domain.ManitoMember;
import com.manytooneserverjava.manitomember.domain.ManitoMemberRepository;
import com.manytooneserverjava.manitomember.web.ManitoCreateForm;
import com.manytooneserverjava.member.domain.Member;
import com.manytooneserverjava.member.domain.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManitoService {
    private final MemberRepository memberRepository;
    private final ManitoRepository manitoRepository;
    private final ManitoMemberRepository manitoMemberRepository;

    /**
     * 입력한 요청값을 바탕으로 마니또 모임을 형성하고 생성자를 마니또 모임장으로 추가하는 매서드
     * @param memberId
     * @param form
     * @return 저장된 마니또 객체의 ID를 반환
     */
    @Transactional
    public Long createManito(Long memberId, ManitoCreateForm form) {
        // 마니또 생성 로직
        Manito manito = Manito.builder()
                .name(form.name())
                .endDateTime(form.endDateTime())
                .build();
        Manito savedManito = manitoRepository.save(manito);

        // 마니또 회원 추가 로직 (최초 마니또 생성자는 리더가 된다.)
        Member findMember = memberRepository.findById(memberId).get();
        ManitoMember manitoMember = ManitoMember.builder()
                .member(findMember)
                .manito(savedManito)
                .status(1) // 초대 받은 상태
                .isLeader(true) // 리더여부에 true로 설정
                .build();
        manitoMemberRepository.save(manitoMember);
        return savedManito.getId();
    }


    /**
     * 마니또 모임 활성화 로직
     * 활성화 시 참가 인원들에 대한 마니도 매칭
     * @param manitoId
     */
    @Transactional
    public void initiateManito(Long manitoId) {
        // 가입된 마니또 회원 목록 조회
        List<ManitoMember> manitoMembersInSameGroup = manitoMemberRepository.findManitoMembersInSameGroup(manitoId);
        // 마니또 매칭 코드
        HashMap<ManitoMember, ManitoMember> match = matchManito(manitoMembersInSameGroup);
        // 마니또 매칭 내역 적용
        for (ManitoMember manitoMember : match.keySet()) manitoMember.editMyManito(match.get(manitoMember).getMember());
        // 마니또 모임 상태 변경
        Manito findManito = manitoRepository.findById(manitoId).get();
        findManito.updateManito(true, findManito.getEndDateTime());
    }

    /**
     * 마니또 모임 준비 종료 매서드
     * 종료됨에 따라 회원들은 모든 회원들의 마니또를 확인할 수 있다.
     * @param manitoId
     */
    @Transactional
    public void endManito(Long manitoId) {
        Manito findManito = manitoRepository.findById(manitoId).get();
        findManito.updateManito(false, findManito.getEndDateTime());
    }


    private HashMap<ManitoMember, ManitoMember> matchManito(List<ManitoMember> manitoMembersInSameGroup) {
        int[] ch = new int[manitoMembersInSameGroup.size()];
        HashMap<ManitoMember, ManitoMember> match = new HashMap<>();
        int cnt = manitoMembersInSameGroup.size();
        for (ManitoMember manitoMember : manitoMembersInSameGroup) {
            while (true) {
                int idx = (int) (Math.random() * cnt);
                ManitoMember fetchMember = manitoMembersInSameGroup.get(idx);
                if (fetchMember != manitoMember && ch[idx] == 0) {
                    match.put(manitoMember, fetchMember);
                    ch[idx] = 1;
                    break;
                }
            }
        }
        return match;
    }
}