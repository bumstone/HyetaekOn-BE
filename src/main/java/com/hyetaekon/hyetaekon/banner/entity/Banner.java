package com.hyetaekon.hyetaekon.banner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "banner")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 배너 ID

    @Column(name = "title", nullable = false, length = 100)
    private String title; // 배너 제목

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl; // 배너 이미지 URL

    @Column(name = "link_url", length = 500)
    private String linkUrl; // 배너 클릭 시 이동할 URL

    @Column(name = "display_order", nullable = false)
    private int displayOrder; // 배너 정렬 순서

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 생성일

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 수정일
}
