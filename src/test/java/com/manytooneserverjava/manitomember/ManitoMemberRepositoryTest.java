package com.manytooneserverjava.manitomember;

import com.manytooneserverjava.manito.domain.Manito;
import com.manytooneserverjava.manito.domain.ManitoRepository;
import com.manytooneserverjava.manitomember.domain.ManitoMember;
import com.manytooneserverjava.manitomember.domain.ManitoMemberRepository;
import com.manytooneserverjava.member.domain.Member;
import com.manytooneserverjava.member.domain.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
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
class ManitoMemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ManitoRepository manitoRepository;

    @Autowired
    ManitoMemberRepository manitoMemberRepository;

    @BeforeEach
    void beforeEach() {
        Member testMember1 = Member.builder()
                .email("testMembe1@naver.com")
                .name("testMember1")
                .passwd("test")
                .build();
        Member testMember2 = Member.builder()
                .email("testMember2@naver.com")
                .name("testMember2")
                .passwd("test")
                .build();
        memberRepository.save(testMember1);
        memberRepository.save(testMember2);

        Manito testManito1 = Manito.builder()
                .name("test1")
                .status(true)
                .endDateTime(LocalDateTime.now().plusHours(3))
                .build();

        Manito testManito2 = Manito.builder()
                .name("test2")
                .status(true)
                .endDateTime(LocalDateTime.now().plusHours(3))
                .build();

        manitoRepository.save(testManito1);
        manitoRepository.save(testManito2);
    }

    @AfterEach
    void afterEach() {
        List<Manito> findManitos = manitoRepository.findAll();
        if (!findManitos.isEmpty()) manitoRepository.deleteAll(findManitos);

        List<Member> findMembers = memberRepository.findAll();
        if (!findMembers.isEmpty()) memberRepository.deleteAll(findMembers);
    }

    @Test
    @Transactional
    void saveTest() {
        Member testMember1 = memberRepository.findByName("testMember1").get();
        Manito testManito1 = manitoRepository.findByName("test1").get();
        ManitoMember testManitoMember1 = ManitoMember.builder()
                .member(testMember1)
                .manito(testManito1)
                .status(1)
                .isLeader(true)
                .build();
        manitoMemberRepository.save(testManitoMember1);
        ManitoMember findManitoMember = manitoMemberRepository.findById(testManitoMember1.getId()).get();
        assertThat(findManitoMember).isEqualTo(testManitoMember1);
    }

    @Test
    @Transactional
    void updateTest() {
        Member testMember1 = memberRepository.findByName("testMember1").get();
        Manito testManito1 = manitoRepository.findByName("test1").get();
        ManitoMember testManitoMember1 = ManitoMember.builder()
                .member(testMember1)
                .manito(testManito1)
                .status(1)
                .isLeader(true)
                .build();
        manitoMemberRepository.save(testManitoMember1);
        testManitoMember1.updateManitoMember(1, "인사이드아웃",
                "야구글러브", "술", true, null);
        ManitoMember findManitoMember = manitoMemberRepository.findById(testManitoMember1.getId()).get();
        assertThat(findManitoMember.getStatus()).isEqualTo(1);
        assertThat(findManitoMember.getNickname()).isEqualTo("인사이드아웃");
        assertThat(findManitoMember.getWantedGift()).isEqualTo("야구글러브");
        assertThat(findManitoMember.getUnwantedGift()).isEqualTo("술");
        assertThat(findManitoMember.getIsLeader()).isEqualTo(true);
    }

    @Test
    @Transactional
    void deleteTest() {
        Member testMember1 = memberRepository.findByName("testMember1").get();
        Manito testManito1 = manitoRepository.findByName("test1").get();
        ManitoMember testManitoMember1 = ManitoMember.builder()
                .member(testMember1)
                .manito(testManito1)
                .status(1)
                .isLeader(true)
                .build();
        manitoMemberRepository.save(testManitoMember1);
        Long id = testManitoMember1.getId();
        manitoMemberRepository.delete(testManitoMember1);
        assertThatThrownBy(() -> manitoMemberRepository.findById(id).get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @Transactional
    void selectAllTest() {
        Member testMember1 = memberRepository.findByName("testMember1").get();
        Manito testManito1 = manitoRepository.findByName("test1").get();
        Manito testManito2 = manitoRepository.findByName("test2").get();
        ManitoMember testManitoMember1 = ManitoMember.builder()
                .member(testMember1)
                .manito(testManito1)
                .status(1)
                .isLeader(true)
                .build();
        ManitoMember testManitoMember2 = ManitoMember.builder()
                .member(testMember1)
                .manito(testManito1)
                .status(1)
                .isLeader(true)
                .build();
        manitoMemberRepository.save(testManitoMember1);
        manitoMemberRepository.save(testManitoMember2);
        assertThat(manitoMemberRepository.findAll().size()).isEqualTo(2);
    }
}