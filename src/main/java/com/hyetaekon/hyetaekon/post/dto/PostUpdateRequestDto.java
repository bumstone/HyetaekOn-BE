package com.hyetaekon.hyetaekon.post.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequestDto {
    private Long nickName;
    private String title;
    private String content;
    // private LocalDateTime createdAt;
    private String postType;
    private String urlTitle;
    private String urlPath;
    private String tags;
    private List<String> imageUrls; // ✅ 이미지 URL 리스트
}
