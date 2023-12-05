package com.manytooneserverjava.memberrelation.domain;

import com.manytooneserverjava.common.persistence.BaseEntity;
import com.manytooneserverjava.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberRelation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_RELATION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(referencedColumnName = "MEMBER_ID", name = "INVITING_MEMBER_ID", nullable = false)
    private Member invitingMember;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(referencedColumnName = "MEMBER_ID", name = "INVITED_MEMBER_ID", nullable = false)
    private Member invitedMember;

    @Column(name = "MEMBER_RELATION_TYPE", nullable = false)
    private Integer status;

    public void updateMemberRelation(Integer statusUpdateParam) {
        this.status = statusUpdateParam;
    }
}
