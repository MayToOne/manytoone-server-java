package com.manytooneserverjava.manitomember.domain;

import com.manytooneserverjava.manito.domain.Manito;

import java.util.List;
import java.util.Optional;

public interface ManitoMemberRepositoryCustom {
    List<ManitoMember> findManitoMembersInSameGroup(Long manitoId, Integer status);

    List<ManitoMember> findMyManitoInvites(Long memberId);

    Optional<ManitoMember> findByMemberIdAndManitoId(Long memberId, Long manitoId);

    List<Manito> findMyManito(Long memberId);
}
