package com.hyetaekon.hyetaekon.common.util;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME(0)")
  private LocalDateTime createdAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "suspend_at")
  private LocalDateTime suspendAt;

  // 삭제 처리
  public void delete() {
    this.deletedAt = LocalDateTime.now();
  }

  // 정지 처리
  public void suspend() {
    this.suspendAt = LocalDateTime.now();
  }

  // 복원시
  public void restore() {
    this.deletedAt = null;
    this.suspendAt = null;
  }
}