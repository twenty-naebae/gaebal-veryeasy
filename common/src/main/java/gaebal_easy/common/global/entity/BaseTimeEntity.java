package gaebal_easy.common.global.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    // 생성 시점에 자동 설정
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (createdBy == null) {
            createdBy =  getUserIdFromRequest();  // 실제 사용자 정보
        }
        updatedAt = null;
        updatedBy = null;
    }

    // 수정 시점에 자동 설정
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        updatedBy =  getUserIdFromRequest();  // 실제 사용자 정보
    }

    private String getUserIdFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String userId = request.getHeader("userId");

            if(userId != null){
                return userId;
            }
        }
        return "ANONYMOUS";
    }

    public void rollbackDelete(String updatedBy) {
        this.isDeleted = false;
        this.deletedBy = null;
        this.deletedAt = null;
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }
}