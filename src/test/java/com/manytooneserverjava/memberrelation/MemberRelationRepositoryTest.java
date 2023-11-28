package com.manytooneserverjava.memberrelation;

import com.manytooneserverjava.member.domain.Member;
import com.manytooneserverjava.member.domain.MemberRepository;
import com.manytooneserverjava.memberrelation.domain.MemberRelation;
import com.manytooneserverjava.memberrelation.domain.MemberRelationRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MemberRelationRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberRelationRepository memberRelationRepository;

    @BeforeEach
    void beforeEach() {
        Member fromMember = Member.builder()
                .email("fromMember@naver.com")
                .name("fromMember")
                .passwd("test")
                .build();
        Member toMember = Member.builder()
                .email("toMember@naver.com")
                .name("toMember")
                .passwd("test")
                .build();
        memberRepository.save(fromMember);
        memberRepository.save(toMember);
    }

    @AfterEach
    void afterEach() {
        List<Member> findMembers = memberRepository.findAll();
        if (!findMembers.isEmpty()) memberRepository.deleteAll(findMembers);
    }

    @Test
    @Transactional
    void saveTest() {
        Member fromMember = memberRepository.findByName("fromMember").get();
        Member toMember = memberRepository.findByName("toMember").get();
        System.out.println("fromMember = " + fromMember);
        System.out.println("toMember = " + toMember);
        MemberRelation memberRelation = MemberRelation.builder()
                .invitingMember(fromMember)
                .invitedMember(toMember)
                .status(0)
                .build();
        MemberRelation savedMemberRelation = memberRelationRepository.save(memberRelation);
        MemberRelation findMememberRelation = memberRelationRepository.findById(savedMemberRelation.getId()).get();
        assertThat(savedMemberRelation).isEqualTo(findMememberRelation);
    }

    @Test
    @Transactional
    void updateTest() {
        Member fromMember = memberRepository.findByName("fromMember").get();
        Member toMember = memberRepository.findByName("toMember").get();
        MemberRelation memberRelation = MemberRelation.builder()
                .invitingMember(fromMember)
                .invitedMember(toMember)
                .status(0)
                .build();
        MemberRelation savedMemberRelation = memberRelationRepository.save(memberRelation);
        int statusUpdateParam = -1;
        savedMemberRelation.updateMemberRelation(statusUpdateParam);
        MemberRelation findMememberRelation = memberRelationRepository.findById(savedMemberRelation.getId()).get();
        assertThat(findMememberRelation.getStatus()).isEqualTo(statusUpdateParam);
    }

    @Test
    @Transactional
    void deleteTest() {
        Member fromMember = memberRepository.findByName("fromMember").get();
        Member toMember = memberRepository.findByName("toMember").get();
        MemberRelation memberRelation = MemberRelation.builder()
                .invitingMember(fromMember)
                .invitedMember(toMember)
                .status(0)
                .build();
        MemberRelation savedMemberRelation = memberRelationRepository.save(memberRelation);
        Long id = savedMemberRelation.getId();
        memberRelationRepository.delete(savedMemberRelation);
        assertThatThrownBy(() -> memberRelationRepository.findById(id).get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @Transactional
    void selectAllTest() {
        Member fromMember = memberRepository.findByName("fromMember").get();
        Member toMember = memberRepository.findByName("toMember").get();
        MemberRelation memberRelation = MemberRelation.builder()
                .invitingMember(fromMember)
                .invitedMember(toMember)
                .status(0)
                .build();
        memberRelationRepository.save(memberRelation);
        List<MemberRelation> result = memberRelationRepository.findAll();
        assertThat(result.size()).isEqualTo(1);
    }

}
