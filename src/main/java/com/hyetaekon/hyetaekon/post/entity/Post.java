package com.hyetaekon.hyetaekon.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 게시글 ID

    private Long userId;  // 회원 ID
    private Long publicServiceId; // 공공서비스 ID
    private String title;  // 제목
    private String content; // 내용

    private LocalDateTime createdAt;  // 생성일
    private String postType;  // 게시글 구분
    private LocalDateTime deletedAt;  // 삭제일
    private String serviceUrl;  // 서비스 상세경로
    private int recommendCnt;  // 추천수
}
