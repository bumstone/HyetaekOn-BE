package com.hyetaekon.hyetaekon.comment.entity;

import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long parentId;  // 대댓글이면 부모 댓글 ID, 아니면 null

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at", nullable = false)
    private LocalDateTime deletedAt;

    @Column(name = "suspend_at", nullable = false)
    private LocalDateTime suspendAt;

    // ⭐ 생성 시점에 createdAt 자동 설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
