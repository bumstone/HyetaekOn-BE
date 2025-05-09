package com.hyetaekon.hyetaekon.post.entity;

import com.hyetaekon.hyetaekon.bookmark.entity.Bookmark;
import com.hyetaekon.hyetaekon.common.util.BaseEntity;
import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import com.hyetaekon.hyetaekon.recommend.entity.Recommend;
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
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 게시글 ID

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "public_service_id")
    private PublicService publicService;

    @Column(columnDefinition = "VARCHAR(20) CHARACTER SET utf8mb4", nullable = false)  // ✅ 제목 20자 제한
    private String title;

    @Column(columnDefinition = "VARCHAR(500) CHARACTER SET utf8mb4", nullable = false)  // ✅ 내용 500자 제한
    private String content;

    @Builder.Default
    @Column(name = "recommend_cnt")
    private int recommendCnt = 0;  // 추천수

    @Builder.Default
    @Column(name = "view_count")
    private int viewCnt = 0;  // 조회수

    // TODO: 댓글 생성/수정 시 업데이트
    @Builder.Default
    @Column(name = "comment_cnt")
    private int commentCnt = 0;   // 댓글수

    @Column(name = "post_type", nullable = false)
    @Enumerated(EnumType.STRING)  // ✅ ENUM 타입으로 저장 (질문, 자유, 인사)
    private PostType postType;

    private String serviceUrl;

    @Column(columnDefinition = "VARCHAR(12) CHARACTER SET utf8mb4")  // ✅ url제목 12자 제한
    private String urlTitle;

    private String urlPath;

    @Column(length = 255)
    private String tags; // ✅ 태그는 최대 3개 (쉼표 구분)

    @Column(name = "category_id")
    private Long categoryId;


    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();  // ✅ 게시글 이미지와 연결

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Recommend> recommends = new ArrayList<>();

    // 조회수 증가
    public void incrementViewCnt() {
        this.viewCnt++;
    }

    // 추천수 증가
    public void incrementRecommendCnt() {
        this.recommendCnt++;
    }

    // 추천수 감소
    public void decrementRecommendCnt() {
        this.recommendCnt = Math.max(0, this.recommendCnt - 1);
    }

    public void incrementCommentCnt() {
        this.commentCnt++;
    }

    public void decrementCommentCnt() {
        this.commentCnt = Math.max(0, this.commentCnt - 1);
    }


    public String getDisplayContent() {
        if (getDeletedAt() != null) {
            return "삭제된 게시글입니다.";
        } else if (getSuspendAt() != null) {
            return "관리자에 의해 삭제된 게시글입니다.";
        }
        return content;
    }

}
