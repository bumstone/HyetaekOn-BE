package com.hyetaekon.hyetaekon.answer.entity;

import com.hyetaekon.hyetaekon.common.util.BaseEntity;
import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.user.entity.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "answer")
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 답변 ID

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content; // 답변 내용

    @Column(name = "selected", nullable = false)
    private boolean selected; // 채택 여부

    public String getDisplayContent() {
        if (getDeletedAt() != null) {
            return "삭제된 답변입니다.";
        } else if (getSuspendAt() != null) {
            return "관리자에 의해 삭제된 답변입니다.";
        }
        return content;
    }
}
