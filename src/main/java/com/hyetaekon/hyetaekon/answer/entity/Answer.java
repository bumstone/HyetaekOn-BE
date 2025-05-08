package com.hyetaekon.hyetaekon.answer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 답변 ID

    @Column(name = "post_id", nullable = false)
    private Long postId; // 게시글 ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 회원 ID

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content; // 답변 내용

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 생성일

    @Column(name = "selected", nullable = false)
    private boolean selected; // 채택 여부

    // ⭐ 생성 시점에 createdAt 자동 설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
