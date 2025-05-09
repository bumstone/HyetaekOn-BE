package com.hyetaekon.hyetaekon.comment.entity;

import com.hyetaekon.hyetaekon.common.util.BaseEntity;
import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment extends BaseEntity {
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

    public String getDisplayContent() {
        if (getDeletedAt() != null) {
            return "삭제된 댓글입니다.";
        } else if (getSuspendAt() != null) {
            return "관리자에 의해 삭제된 댓글입니다.";
        }
        return content;
    }

}
