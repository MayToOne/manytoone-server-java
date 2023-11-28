package com.manytooneserverjava.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public Optional<Member> findByName(String name);

    public Optional<Member> findByEmail(String email);
}
