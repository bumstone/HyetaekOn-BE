package com.hyetaekon.hyetaekon.comment.repository;

import com.hyetaekon.hyetaekon.comment.entity.Comment;
import com.hyetaekon.hyetaekon.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 게시글의 최상위 댓글 조회 (삭제되지 않은 댓글만)
    Page<Comment> findByPostAndParentIdIsNullAndDeletedAtIsNull(Post post, Pageable pageable);

    // 특정 댓글의 대댓글 조회 (삭제되지 않은 댓글만)
    Page<Comment> findByPostAndParentIdAndDeletedAtIsNull(Post post, Long parentId, Pageable pageable);

    // 모든 댓글 조회 (삭제 여부 관계없이)
    Page<Comment> findByPost(Post post, Pageable pageable);
}
