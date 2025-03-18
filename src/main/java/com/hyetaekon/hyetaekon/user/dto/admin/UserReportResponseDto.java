package com.hyetaekon.hyetaekon.user.dto.admin;

import lombok.Getter;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserReportResponseDto {
    private Long id;
    private Long reporterId;
    private String reporterNickname;
    private Long reportedId;
    private String reportedNickname;
    private String reason;
    private String content;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
}
