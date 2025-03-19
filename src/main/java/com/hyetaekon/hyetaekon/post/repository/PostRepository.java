package com.hyetaekon.hyetaekon.post.repository;

import com.hyetaekon.hyetaekon.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByUserIdAndDeletedAtIsNull(Long userId);
}
