package org.prgrms.kyu.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")

    private LocalDateTime updatedAt;

    @PrePersist
    private void onPrePersist() {
        this.createdAt = this.getCreatedAt().truncatedTo(ChronoUnit.MILLIS);
        this.updatedAt = this.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS);
    }
}