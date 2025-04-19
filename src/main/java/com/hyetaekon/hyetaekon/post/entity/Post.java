package com.hyetaekon.hyetaekon.post.entity;

import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import com.hyetaekon.hyetaekon.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 게시글 ID

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "public_service_id")
    private PublicService publicService;

    @Column(length = 50, nullable = false)  // ✅ 제목 25자 제한
    private String title;

    @Column(columnDefinition = "VARCHAR(500) CHARACTER SET utf8mb4", nullable = false)  // ✅ 내용 500자 제한
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime deletedAt;

    private int recommendCnt;  // 추천수

    private int viewCnt;  // 조회수

    // TODO: 댓글 생성/수정 시 업데이트
    private int commentCnt;   // 댓글수

    @Column(name = "post_type", nullable = false)
    @Enumerated(EnumType.STRING)  // ✅ ENUM 타입으로 저장 (질문, 자유, 인사)
    private PostType postType;

    private String serviceUrl;

    @Column(length = 100)
    private String urlTitle;

    private String urlPath;

    @Column(length = 255)
    private String tags; // ✅ 태그는 최대 3개 (쉼표 구분)

    @Column(name = "category_id")
    private Long categoryId;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();  // ✅ 게시글 이미지와 연결

    public void incrementCommentCnt() {
        this.commentCnt++;
    }

    public void decrementCommentCnt() {
        this.commentCnt = Math.max(0, this.commentCnt - 1);
    }
}
