package com.manytooneserverjava.member;

import com.manytooneserverjava.member.domain.Member;
import com.manytooneserverjava.member.domain.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    void saveTest() {
        Member member = Member.builder()
                .email("test@naver.com")
                .name("test1")
                .passwd("passwd")
                .build();
        memberRepository.save(member);
        Member findMember = memberRepository.findById(member.getId()).get();

        assertThat(findMember).isEqualTo(member);
        System.out.println("findMember = " + findMember);
    }

    @Test
    @Transactional
    void updateTest() {
        Member member = Member.builder()
                .email("updateTester@naver.com")
                .name("updateTester")
                .passwd("before")
                .build();
        memberRepository.save(member);
        System.out.println("member = " + member);
        member.updatePasswd("after");
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember.getPasswd()).isEqualTo("after");
        System.out.println("findMember = " + findMember);
    }

    @Test
    @Transactional
    void deleteTest() {
        Member member = Member.builder()
                .email("deleteTester@naver.com")
                .name("deleteTester")
                .passwd("test")
                .build();
        memberRepository.save(member);
        Long id = member.getId();
        memberRepository.delete(member);
        assertThatThrownBy(() -> memberRepository.findById(id).get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @Transactional
    void selectAllTest() {
        Member member1 = Member.builder()
                .email("selectTester1@naver.com")
                .name("selectTester")
                .passwd("test")
                .build();
        Member member2 = Member.builder()
                .email("selectTester2@naver.com")
                .name("selectTester")
                .passwd("test")
                .build();
        Member member3 = Member.builder()
                .email("selectTester3@naver.com")
                .name("selectTester")
                .passwd("test")
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        List<Member> findMembers = memberRepository.findAll();
        assertThat(findMembers.size()).isEqualTo(3);
    }
}
