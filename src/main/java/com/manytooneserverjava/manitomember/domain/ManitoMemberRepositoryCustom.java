package com.manytooneserverjava.manitomember.domain;

import java.util.List;

public interface ManitoMemberRepositoryCustom {
    List<ManitoMember> findManitoMembersInSameGroup(Long manitoId);

    List<ManitoMember> findMyManitoInvites(Long memberId);
}
