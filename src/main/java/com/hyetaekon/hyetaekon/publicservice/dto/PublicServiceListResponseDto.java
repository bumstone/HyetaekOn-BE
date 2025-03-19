package com.hyetaekon.hyetaekon.publicservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PublicServiceListResponseDto {
    private Long publicServiceId;
    private String serviceName;
    private String summaryPurpose;

    // 담당부처
    private String governingAgency;  // 소관기관명
    private String department;  // 부서명


    private String userType;  // 사용자 구분
    private String contactInfo;  // 문의처
    private String applicationDeadline;  // 신청 기한
    private String supportType;  // 지원유형


    // 해시태그
    private String serviceCategory;  // 서비스 분야
    private List<String> specialGroup;  // 특수 대상 그룹
    private List<String> familyType;  // 가구 형태

    private int views;  // 조회수
    private int bookmarkCnt;  // 북마크수
    private boolean bookmarked;  // 북마크 여부
}
