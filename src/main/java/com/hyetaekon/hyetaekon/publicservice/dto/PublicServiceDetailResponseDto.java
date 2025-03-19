package com.hyetaekon.hyetaekon.publicservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PublicServiceDetailResponseDto {
    private Long publicServiceId;
    private String serviceName;
    private String summaryPurpose;
    private String governingAgency;
    private String department;
    private String userType;
    private String contactInfo;
    private String applicationDeadline;
    private String supportType;
    private String serviceCategory;
    private List<String> specialGroup;
    private List<String> familyType;
    private int views;
    private int bookmarkCnt;
    private boolean bookmarked;   // 북마크 여부

    // 지원 대상
    private String supportTarget;  // 지원 대상
    private String selectionCriteria;  // 선정 기준

    // 서비스 내용
    private String servicePurpose;  // 서비스 목적
    private String supportDetail;  // 지원 내용
    //private String supportType;  // 지원 유형(공통)

    // 신청 방법
    private String applicationMethod;  // 신청 방법
    //private String applicationDeadline;  // 신청 기한(공통)
    private String requiredDocuments;  // 구비 서류

    // 추가 정보
    //private String contactInfo;  // 문의처(공통)
    private String onlineApplicationUrl;  // 온라인 경로 url
    private String relatedLaws;  // 관련 법률

}
