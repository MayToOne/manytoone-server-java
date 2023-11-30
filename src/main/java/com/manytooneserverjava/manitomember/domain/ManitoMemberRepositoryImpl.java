package com.manytooneserverjava.manitomember.domain;

import com.manytooneserverjava.member.domain.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.manytooneserverjava.manitomember.domain.QManitoMember.manitoMember;
import static com.manytooneserverjava.member.domain.QMember.member;

@RequiredArgsConstructor
public class ManitoMemberRepositoryImpl implements ManitoMemberRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<ManitoMember> findManitoMembersInSameGroup(Long manitoId) {
        return query
                .select(manitoMember)
                .from(manitoMember)
                .where((manitoMember.manito.id.eq(manitoId))
                        .and(manitoMember.status.eq(1)))
                .fetch();
    }

    @Override
    public List<ManitoMember> findMyManitoInvites(Long memberId) {
        return query
                .select(manitoMember)
                .from(manitoMember)
                .join(member)
                .on(manitoMember.member.id.eq(member.id))
                .where(manitoMember.member.id.eq(memberId))
                .fetch();
    }
}
