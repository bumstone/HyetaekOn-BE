package com.hyetaekon.hyetaekon.post.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    private LocalDateTime deletedAt;

    // 이미지가 삭제되었는지 확인하는 메소드
    public boolean isDeleted() {
        return deletedAt != null;
    }

    // Soft delete 처리 메소드
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
