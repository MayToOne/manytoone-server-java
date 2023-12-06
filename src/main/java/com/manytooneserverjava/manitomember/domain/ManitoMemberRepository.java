package com.manytooneserverjava.manitomember.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManitoMemberRepository extends JpaRepository<ManitoMember, Long>, ManitoMemberRepositoryCustom{
}
