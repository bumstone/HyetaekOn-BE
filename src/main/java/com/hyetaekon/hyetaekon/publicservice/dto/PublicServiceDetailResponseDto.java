package com.hyetaekon.hyetaekon.publicservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PublicServiceDetailResponseDto extends PublicServiceListResponseDto {
    //private Long publicServiceId;
    //private String serviceName;  // 서비스명(공통)

    // 지원 대상
    private String supportTarget;  // 지원 대상
    private String selectionCriteria;  // 선정 기준

    // 서비스 내용
    private String servicePurpose;  // 서비스 목적
    private String supportDetails;  // 지원 내용
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
