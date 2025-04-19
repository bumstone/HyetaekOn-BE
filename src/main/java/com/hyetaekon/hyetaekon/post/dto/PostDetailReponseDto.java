package com.hyetaekon.hyetaekon.post.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailReponseDto {
    private Long postId;
    private Long nickName;  // 작성자 닉네임
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int recommendCnt;
    private int viewCnt;
    private String urlTitle;
    private String urlPath;
    private String tags;
    private List<String> imageUrls; // ✅ 이미지 URL 리스트 추가
}
