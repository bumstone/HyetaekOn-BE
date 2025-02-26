package com.hyetaekon.hyetaekon.post.repository;

import com.hyetaekon.hyetaekon.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryId(Long categoryId, Pageable pageable);
}