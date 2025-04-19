package com.hyetaekon.hyetaekon.post.service;

import com.hyetaekon.hyetaekon.post.dto.*;
import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.post.entity.PostImage;
import com.hyetaekon.hyetaekon.post.entity.PostType;
import com.hyetaekon.hyetaekon.post.mapper.PostMapper;
import com.hyetaekon.hyetaekon.post.repository.PostRepository;
import com.hyetaekon.hyetaekon.recommend.repository.RecommendRepository;
import com.hyetaekon.hyetaekon.user.entity.User;
import com.hyetaekon.hyetaekon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RecommendRepository recommendRepository;
    private final PostMapper postMapper;

    /**
     * 전체 게시글 목록 조회 (페이징)
     */
    public Page<PostListResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findByDeletedAtIsNull(pageable)
            .map(postMapper::toPostListDto);
    }

    /**
     * 특정 타입의 게시글 목록 조회 (페이징)
     */
    public Page<PostListResponseDto> getPostsByType(PostType postType, Pageable pageable) {
        return postRepository.findByPostTypeAndDeletedAtIsNull(postType, pageable)
            .map(postMapper::toPostListDto);
    }

    /**
     * 특정 게시글 상세 조회(로그인 시)
     */
    @Transactional
    public PostDetailReponseDto getPostById(Long postId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
            .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다: " + postId));

        // 조회수 증가
        post.incrementViewCnt();

        // 사용자의 추천 여부 확인
        boolean recommended = recommendRepository.existsByUserIdAndPostId(userId, postId);

        // DTO 변환 및 추천 여부 설정
        PostDetailReponseDto responseDto = postMapper.toPostDetailDto(post);
        responseDto.setRecommended(recommended); // DTO에 isRecommended 필드 추가 필요

        return postMapper.toPostDetailDto(post);
    }

    /**
     * 게시글 생성(로그인 시)
     */
    @Transactional
    public PostDetailReponseDto createPost(PostCreateRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        // PostType Enum 변환
        PostType postType = findPostTypeByName(requestDto.getPostType());

        // DTO -> Entity 변환
        Post post = postMapper.toEntity(requestDto);
        post.setUser(user);
        post.setPostType(postType);

        // 이미지 URL 처리
        if (requestDto.getImageUrls() != null && !requestDto.getImageUrls().isEmpty()) {
            List<PostImage> postImages = new ArrayList<>();

            for (String imageUrl : requestDto.getImageUrls()) {
                PostImage postImage = PostImage.builder()
                    .post(post)
                    .imageUrl(imageUrl)
                    .build();
                postImages.add(postImage);
            }

            post.setPostImages(postImages);
        }

        Post savedPost = postRepository.save(post);
        return postMapper.toPostDetailDto(savedPost);
    }

    /**
     * 게시글 수정 (본인만 가능)
     */
    @Transactional
    public PostDetailReponseDto updatePost(Long postId, PostUpdateRequestDto updateDto, Long userId) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
            .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다: " + postId));

        // 작성자 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("게시글 수정 권한이 없습니다");
        }

        // PostType 변환
        if (updateDto.getPostType() != null) {
            PostType postType = findPostTypeByName(updateDto.getPostType());
            post.setPostType(postType);
        }

        // 기본 정보 업데이트
        postMapper.updatePostFromDto(updateDto, post);

        // 이미지 업데이트 처리
        if (updateDto.getImageUrls() != null) {
            // 기존 이미지 제거
            post.getPostImages().clear();

            // 새 이미지 추가
            List<PostImage> postImages = updateDto.getImageUrls().stream()
                .map(imageUrl -> PostImage.builder()
                    .post(post)
                    .imageUrl(imageUrl)
                    .build())
                .collect(Collectors.toList());

            post.getPostImages().addAll(postImages);
        }

        return postMapper.toPostDetailDto(post);
    }

    /**
     * 게시글 삭제 (본인 또는 관리자만 가능)
     */
    @Transactional
    public void deletePost(Long postId, Long userId, String role) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
            .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다: " + postId));

        // 작성자 또는 관리자 확인
        boolean isOwner = post.getUser().getId().equals(userId);
        boolean isAdmin = "ROLE_ADMIN".equals(role);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("게시글 삭제 권한이 없습니다");
        }

        // Soft Delete 처리
        post.setDeletedAt(LocalDateTime.now());
    }

    /**
     * 게시글 타입명으로 Enum 조회
     */
    private PostType findPostTypeByName(String postTypeName) {
        try {
            // 한글명으로 찾기
            for (PostType type : PostType.values()) {
                if (type.getKoreanName().equals(postTypeName)) {
                    return type;
                }
            }

            // Enum 상수명으로 찾기
            return PostType.valueOf(postTypeName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 게시글 타입입니다: " + postTypeName);
        }
    }


}
