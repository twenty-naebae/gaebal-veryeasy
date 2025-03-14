package com.gaebal_easy.client.user.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String createdBy;  // 생성한 유저

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;  // 생성 시간

    @LastModifiedBy
    private String updatedBy;  // 수정한 유저

    @LastModifiedDate
    private LocalDateTime updatedAt;  // 수정 시간

    private boolean isDeleted; //삭제 여부

    private LocalDateTime deletedAt;  // 삭제 시간 (Soft Delete)

    private String deletedBy;  // 삭제한 유저

    // 소프트 삭제 처리
    public void delete(String deletedBy) {
        this.isDeleted = true;
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }
}