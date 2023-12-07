package com.manytooneserverjava.manitomember;

import com.manytooneserverjava.manito.domain.Manito;
import com.manytooneserverjava.manito.domain.ManitoRepository;
import com.manytooneserverjava.manito.service.ManitoService;
import com.manytooneserverjava.manito.web.dto.ManitoCreateForm;
import com.manytooneserverjava.manito.web.dto.ManitoDto;
import com.manytooneserverjava.manitomember.domain.ManitoMember;
import com.manytooneserverjava.manitomember.domain.ManitoMemberRepository;
import com.manytooneserverjava.manitomember.service.ManitoMemberService;
import com.manytooneserverjava.manitomember.web.dto.GiftInfoForm;
import com.manytooneserverjava.manitomember.web.dto.ManitoMemberDto;
import com.manytooneserverjava.member.domain.Member;
import com.manytooneserverjava.member.domain.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ManitoMemberServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ManitoRepository manitoRepository;
    @Autowired
    ManitoMemberRepository manitoMemberRepository;
    @Autowired
    ManitoMemberService manitoMemberService;
    @Autowired
    ManitoService manitoService;

    @BeforeEach
    void beforeEach() {
        Member testMember1 = Member.builder()
                .name("tester01")
                .passwd("testing")
                .email("tester01")
                .build();
        Member savedMember1 = memberRepository.save(testMember1);

        Member testMember2 = Member.builder()
                .name("tester02")
                .passwd("testing")
                .email("tester02")
                .build();
        Member savedMember2 = memberRepository.save(testMember2);

        ManitoCreateForm createForm = new ManitoCreateForm(savedMember1.getId(), "testManito1", LocalDateTime.now().plusDays(3));
        manitoService.createManito(createForm);

        createForm = new ManitoCreateForm(savedMember2.getId(), "testManito2", LocalDateTime.now().plusDays(3));
        manitoService.createManito(createForm);
    }

    @Test
    @Transactional
    void inviteManitoMember() {
        Member testMember1 = memberRepository.findByName("tester01").get();
        Member testMember2 = memberRepository.findByName("tester02").get();

        List<Manito> all = manitoRepository.findAll();
        for (Manito manito : all) {
            System.out.println("manito = " + manito);
        }
        Manito testManito1 = manitoRepository.findByName("testManito1").get();
        Manito testManito2 = manitoRepository.findByName("testManito2").get();


        manitoMemberService.inviteManitoMember(testManito1.getId(), testMember2.getId());
        List<ManitoMember> findManitoMember1 = manitoMemberRepository.findManitoMembersInSameGroup(testManito1.getId(), null);
        List<ManitoMember> findManitoMember2 = manitoMemberRepository.findManitoMembersInSameGroup(testManito2.getId(), null);
        assertThat(findManitoMember1.size()).isEqualTo(2);
        assertThat(findManitoMember2.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    void updateManitoMemberStatusAccept() {
        Member testMember2 = memberRepository.findByName("tester02").get();
        Manito testManito1 = manitoRepository.findByName("testManito1").get();
        Long manitoMemberId = manitoMemberService.inviteManitoMember(testManito1.getId(), testMember2.getId());
        manitoMemberService.updateManitoMemberStatus(manitoMemberId, 1);
        List<ManitoDto> myManito = manitoMemberService.findMyManitos(testMember2.getId());
        assertThat(myManito.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void updateManitoMemberStatusDecline() {
        Member testMember2 = memberRepository.findByName("tester02").get();
        Manito testManito1 = manitoRepository.findByName("testManito1").get();
        Long manitoMemberId = manitoMemberService.inviteManitoMember(testManito1.getId(), testMember2.getId());
        manitoMemberService.updateManitoMemberStatus(manitoMemberId, -1);
        List<ManitoDto> myManito = manitoMemberService.findMyManitos(testMember2.getId());
        assertThatThrownBy(() -> manitoMemberRepository.findById(manitoMemberId).get()).isInstanceOf(NoSuchElementException.class);
        assertThat(myManito.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    void findMyManitoInvites() {
        Member testMember2 = memberRepository.findByName("tester02").get();
        Manito testManito1 = manitoRepository.findByName("testManito1").get();
        ManitoMember manitoMember = ManitoMember.builder()
                .member(testMember2)
                .manito(testManito1)
                .status(0)
                .isLeader(false)
                .build();
        manitoMemberRepository.save(manitoMember);
        List<ManitoMemberDto> myManitoInvites = manitoMemberService.findMyManitoInvites(testMember2.getId());
        assertThat(myManitoInvites.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void saveGiftInfo() {
        Member testMember2 = memberRepository.findByName("tester02").get();
        Manito testManito2 = manitoRepository.findByName("testManito2").get();
        ManitoMember testManitoMember = manitoMemberRepository.findByMemberIdAndManitoId(testMember2.getId(), testManito2.getId(), 1).get();
        manitoMemberService.saveGiftInfo(new GiftInfoForm(testManitoMember.getId(), "LG 스탠드 바이 미", "주류"));
        assertThat(testManitoMember.getWantedGift()).isEqualTo("LG 스탠드 바이 미");
        assertThat(testManitoMember.getUnwantedGift()).isEqualTo("주류");

        manitoMemberService.saveGiftInfo(new GiftInfoForm(testManitoMember.getId(), "LG 스탠드 바이 미", null));
        assertThat(testManitoMember.getWantedGift()).isEqualTo("LG 스탠드 바이 미");
        assertThat(testManitoMember.getUnwantedGift()).isNull();
    }

    @Test
    @Transactional
    void findMyManito() {
        Member testMember2 = memberRepository.findByName("tester02").get();
        Manito testManito1 = manitoRepository.findByName("testManito1").get();
        Long manitoMemberId = manitoMemberService.inviteManitoMember(testManito1.getId(), testMember2.getId());

        List<ManitoDto> myManitosBeforeAccept = manitoMemberService.findMyManitos(testMember2.getId());
        assertThat(myManitosBeforeAccept.size()).isEqualTo(1);

        manitoMemberService.updateManitoMemberStatus(manitoMemberId, 1);
        List<ManitoDto> myManitosAfterAccept = manitoMemberService.findMyManitos(testMember2.getId());
        assertThat(myManitosAfterAccept.size()).isEqualTo(2);
    }
}