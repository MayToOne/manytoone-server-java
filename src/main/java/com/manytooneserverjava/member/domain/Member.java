package com.manytooneserverjava.member.domain;

import com.manytooneserverjava.common.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "MEMBER_EMAIL", nullable = false)
    private String email;

    @Column(name = "MEMBER_PASSWD", nullable = false)
    private String passwd;

    @Column(name = "MEMBER_NAME", nullable = false)
    private String name;

    public void updatePasswd(String updateParam) {
        this.passwd = updateParam;
    }
}
