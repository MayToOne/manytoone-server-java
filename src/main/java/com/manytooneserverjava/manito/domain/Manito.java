package com.manytooneserverjava.manito.domain;

import com.manytooneserverjava.common.persistence.BaseEntity;
import com.manytooneserverjava.manitomember.domain.ManitoMember;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Manito extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MANITO_ID")
    private Long id;

    @Column(name = "MANITO_NAME", nullable = false)
    private String name;

    @Column(name = "MANITO_END_DATETIME", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "MANITO_STATUS", nullable = false)
    private Boolean status;

    @Column(name = "MANITO_INVITE_LINK")
    private String inviteLink;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "manito_id")
    private List<ManitoMember> manitoMembers = new ArrayList<>();

    public void updateManito(Boolean updateStatusParam, LocalDateTime updateEndDateTimeParam) {
        this.status = updateStatusParam;
        this.endDateTime = updateEndDateTimeParam;
    }
}
