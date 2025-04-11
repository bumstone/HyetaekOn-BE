package com.hyetaekon.hyetaekon.post.entity;

import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import com.hyetaekon.hyetaekon.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "posts")
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

    @Column(length = 20, nullable = false)  // ✅ 제목 20자 제한
    private String title;

    @Column(length = 500, nullable = false)  // ✅ 내용 500자 제한
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime deletedAt;

    private int recommendCnt;  // 추천수

    private int viewCount;  // 조회수

    @Column(name = "post_type")
    @Enumerated(EnumType.STRING)  // ✅ ENUM 타입으로 저장 (질문, 자유, 인사)
    private PostType postType;

    private String serviceUrl;

    @Column(length = 12)  // ✅ 관련 링크 제목 12자 제한
    private String urlTitle;

    private String urlPath;

    @Column(length = 255)
    private String tags; // ✅ 태그는 최대 3개 (쉼표 구분)

    @Column(name = "category_id")
    private Long categoryId;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages;  // ✅ 게시글 이미지와 연결
}
