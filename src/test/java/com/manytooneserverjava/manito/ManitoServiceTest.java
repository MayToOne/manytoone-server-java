package com.manytooneserverjava.manito;

import com.manytooneserverjava.manito.domain.Manito;
import com.manytooneserverjava.manito.domain.ManitoRepository;
import com.manytooneserverjava.manito.service.ManitoService;
import com.manytooneserverjava.manito.web.ManitoCreateForm;
import com.manytooneserverjava.manitomember.domain.ManitoMember;
import com.manytooneserverjava.manitomember.domain.ManitoMemberRepository;
import com.manytooneserverjava.manitomember.service.ManitoMemberService;
import com.manytooneserverjava.member.domain.Member;
import com.manytooneserverjava.member.domain.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ManitoServiceTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ManitoService manitoService;

    @Autowired
    ManitoMemberService manitoMemberService;

    @Autowired
    ManitoRepository manitoRepository;

    @Autowired
    ManitoMemberRepository manitoMemberRepository;

    @BeforeEach
    void beforeEach() {
        Member savedMember1 = memberRepository.save(Member.builder()
                .name("tester01")
                .passwd("testing")
                .email("tester01")
                .build());

        Member savedMember2 = memberRepository.save(Member.builder()
                .name("tester02")
                .passwd("testing")
                .email("tester02")
                .build());

        Member savedMember3 = memberRepository.save(Member.builder()
                .name("tester03")
                .passwd("testing")
                .email("tester03")
                .build());

        ManitoCreateForm createForm = new ManitoCreateForm(savedMember1.getId(), "testManito1", LocalDateTime.now().plusDays(3));
        manitoService.createManito(createForm);

        createForm = new ManitoCreateForm(savedMember2.getId(), "testManito2", LocalDateTime.now().plusDays(3));
        manitoService.createManito(createForm);
    }

    @Test
    @Transactional
    void createManito() {
        Member testMember = memberRepository.findByName("tester01").get();
        String manitoName = "testManito";
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(3);
        ManitoCreateForm createForm = new ManitoCreateForm(testMember.getId(), manitoName, endDateTime);

        Long manitoId = manitoService.createManito(createForm);
        Manito findManito = manitoRepository.findById(manitoId).get();
        ManitoMember findManitoMember = manitoMemberRepository.findByMemberIdAndManitoId(testMember.getId(), manitoId, 1).get();

        System.out.println("findManitoMember = " + findManitoMember);
        System.out.println("findManitoMember.getMember() = " + findManitoMember.getMember());

        assertThat(findManito.getEndDateTime()).isEqualTo(endDateTime);
        assertThat(findManito.getName()).isEqualTo(manitoName);
        assertThat(findManitoMember.getMember()).isEqualTo(testMember);
    }

    @Test
    @Transactional
    void initiateManito() {
        Member tester02 = memberRepository.findByName("tester02").get();
        Member tester03 = memberRepository.findByName("tester03").get();
        Manito testManito1 = manitoRepository.findByName("testManito1").get();

        manitoMemberService.inviteManitoMember(testManito1.getId(), tester02.getId());
        manitoMemberService.inviteManitoMember(testManito1.getId(), tester03.getId());

        ManitoMember findManitoMember = manitoMemberRepository.findByMemberIdAndManitoId(tester02.getId(), testManito1.getId(), null).get();
        manitoMemberService.updateManitoMemberStatus(findManitoMember.getId(), 1);
        // 마니또 활성화 전 => 조회 결과 모든 회원이 조회됨.
        assertThat(manitoMemberRepository.findManitoMembersInSameGroup(testManito1.getId(), null).size()).isEqualTo(3);
        manitoService.initiateManito(testManito1.getId());
        // 마니또 활성화 후 => 수락하지 않은 회원의 경우 삭제됨. -> size 1 감소
        assertThat(manitoMemberRepository.findManitoMembersInSameGroup(testManito1.getId(), null).size()).isEqualTo(2);

        List<ManitoMember> manitoMembersInSameGroup = manitoMemberRepository.findManitoMembersInSameGroup(testManito1.getId(), 1);

        // 마니또 매칭 여부 확인
        for (ManitoMember manitoMember : manitoMembersInSameGroup) {
            assertThat(manitoMember.getMyManito()).isNotNull();
        }

        // 마니또 status 활성화 여부 확인
        assertThat(findManitoMember.getStatus()).isEqualTo(1);
    }

    @Test
    @Transactional
    void endManito() {
        Manito testManito = manitoRepository.findByName("testManito1").get();
        manitoService.endManito(testManito.getId());
        Manito findManito = manitoRepository.findById(testManito.getId()).get();
        assertThat(findManito.getStatus()).isEqualTo(true);
    }

    @Test
    @Transactional
    void deleteManito() {
        Manito testManito1 = manitoRepository.findByName("testManito1").get();
        Long id = testManito1.getId();
        manitoService.deleteManito(id);
        assertThat(manitoRepository.findById(id).isEmpty()).isTrue();
    }
}