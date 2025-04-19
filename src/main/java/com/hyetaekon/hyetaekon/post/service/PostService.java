package com.hyetaekon.hyetaekon.post.service;

import com.hyetaekon.hyetaekon.post.dto.PostDto;
import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.post.entity.PostType;
import com.hyetaekon.hyetaekon.post.mapper.PostMapper;
import com.hyetaekon.hyetaekon.post.repository.PostRepository;
import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import com.hyetaekon.hyetaekon.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return postMapper.toDto(post);
    }

    public List<PostDto> getPostsByCategoryId(Long categoryId) { // 추가됨
        return postRepository.findByCategoryId(categoryId)
                .stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    public PostDto createPost(PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        Post savedPost = postRepository.save(post);
        return postMapper.toDto(savedPost);
    }

    public PostDto updatePost(Long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        User user = new User();
        user.setId(postDto.getUserId());
        post.setUser(user);

        if (postDto.getPublicServiceId() != null) {
            PublicService publicService = new PublicService();
            publicService.setId(postDto.getPublicServiceId()); // String 타입으로 변환됨
            post.setPublicService(publicService);
        } else {
            post.setPublicService(null);
        }

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setPostType(PostType.valueOf(postDto.getPostType()));
        post.setDeletedAt(postDto.getDeletedAt());
        post.setServiceUrl(postDto.getServiceUrl());
        post.setRecommendCnt(postDto.getRecommendCnt());
        post.setViewCnt(postDto.getViewCnt());
        post.setUrlTitle(postDto.getUrlTitle());
        post.setUrlPath(postDto.getUrlPath());
        post.setTags(postDto.getTags());
        post.setCategoryId(postDto.getCategoryId()); // 추가됨
        Post updatedPost = postRepository.save(post);
        return postMapper.toDto(updatedPost);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
