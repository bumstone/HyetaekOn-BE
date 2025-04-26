package com.hyetaekon.hyetaekon.post.repository;

import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.post.entity.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 삭제되지 않은 모든 게시글 조회 (페이징)
    Page<Post> findByDeletedAtIsNull(Pageable pageable);

    // 특정 타입의 삭제되지 않은 게시글 조회 (페이징)
    Page<Post> findByPostTypeAndDeletedAtIsNull(PostType postType, Pageable pageable);

    // ID로 삭제되지 않은 게시글 조회
    Optional<Post> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByUser_IdAndDeletedAtIsNull(Long userId);
}
