package com.hyetaekon.hyetaekon.post.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostDto {
    private Long id;
    private Long userId;
    private String publicServiceId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt; // 삭제일 추가
    private String postType;
    private String serviceUrl;
    private int recommendCnt;
    private int viewCount;
    private String urlTitle;
    private String urlPath;
    private String tags;
    private Long categoryId; // 추가됨
    private List<String> imageUrls; // ✅ 이미지 URL 리스트 추가
}
