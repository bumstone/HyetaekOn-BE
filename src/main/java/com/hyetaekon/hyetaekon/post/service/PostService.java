package com.hyetaekon.hyetaekon.post.service;

import com.hyetaekon.hyetaekon.post.dto.PostDto;
import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.post.mapper.PostMapper;
import com.hyetaekon.hyetaekon.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<PostDto> getPosts(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByCategoryId(categoryId, pageable)
                .stream()
                .map(PostMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public PostDto getPost(Long postId) {
        return postRepository.findById(postId)
                .map(PostMapper.INSTANCE::toDto)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }

    public PostDto createPost(Long categoryId, PostDto postDto) {
        Post post = PostMapper.INSTANCE.toEntity(postDto);
        post.setCategoryId(categoryId);
        return PostMapper.INSTANCE.toDto(postRepository.save(post));
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public void deletePosts(List<Long> postIds) {
        postRepository.deleteAllById(postIds);
    }
}
