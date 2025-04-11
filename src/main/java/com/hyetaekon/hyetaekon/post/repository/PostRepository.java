package com.hyetaekon.hyetaekon.post.repository;

import com.hyetaekon.hyetaekon.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryId(Long categoryId); // 추가됨
}
