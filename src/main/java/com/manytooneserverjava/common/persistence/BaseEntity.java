package com.manytooneserverjava.common.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    @Column(name = "created_datetime", nullable = false)
    private LocalDateTime createDateTime;

    @LastModifiedDate
    @Column(name = "last_modified_datetime")
    private LocalDateTime lastModifiedDateTime;

    @LastModifiedBy
    @Column(name = "last_modified_user")
    private String lastModifiedBy;
}
