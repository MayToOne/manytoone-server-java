package com.manytooneserverjava.manitomember.domain;

import com.manytooneserverjava.manito.domain.Manito;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.manytooneserverjava.manito.domain.QManito.manito;
import static com.manytooneserverjava.manitomember.domain.QManitoMember.manitoMember;
import static com.manytooneserverjava.member.domain.QMember.member;

@RequiredArgsConstructor
public class ManitoMemberRepositoryImpl implements ManitoMemberRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public List<ManitoMember> findManitoMembersInSameGroup(Long manitoId, Integer status) {
        return query
                .select(manitoMember)
                .from(manitoMember)
                .join(manitoMember.member, member).on(manitoMember.member.id.eq(member.id))
                .join(manitoMember.manito, manito).on(manitoMember.manito.id.eq(manito.id))
                .where((manitoMember.manito.id.eq(manitoId))
                        .and(eqStatus(status)))
                .fetch();
    }

    @Override
    public List<ManitoMember> findMyManitoInvites(Long memberId) {
        return query
                .select(manitoMember)
                .from(manitoMember)
                .join(manitoMember.member, member).on(manitoMember.member.id.eq(member.id))
                .on(manitoMember.member.id.eq(member.id))
                .where(manitoMember.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public Optional<ManitoMember> findByMemberIdAndManitoId(Long memberId, Long manitoId, Integer status) {
        ManitoMember result = query
                .select(manitoMember)
                .from(manitoMember)
                .join(manitoMember.member, member).on(manitoMember.member.id.eq(member.id))
                .join(manitoMember.manito, manito).on(manitoMember.manito.id.eq(manito.id))
                .where(
                        (member.id.eq(memberId))
                                .and(manito.id.eq(manitoId))
                                .and(eqStatus(status)))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public List<Manito> findMyManito(Long memberId) {
        List<ManitoMember> result = query.select(manitoMember)
                .from(manitoMember)
                .join(manitoMember.member, member).on(manitoMember.member.id.eq(member.id))
                .join(manitoMember.manito, manito).on(manitoMember.manito.id.eq(manito.id))
                .where(manitoMember.member.id.eq(memberId)
                        .and(manitoMember.status.eq(1)))
                .fetch();
        List<Manito> manitos = result.stream().map(ManitoMember::getManito).collect(Collectors.toList());
        return manitos;
    }

    private BooleanExpression eqStatus(Integer status) {
        if (status == null) {
            return null;
        }
        return manitoMember.status.eq(status);
    }
}
