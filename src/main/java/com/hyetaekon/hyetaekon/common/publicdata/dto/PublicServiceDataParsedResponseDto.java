package com.hyetaekon.hyetaekon.common.publicdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 임시
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicServiceDataParsedResponseDto {
    private long serviceId;
    private String serviceName;
    private String serviceCategory;
    private String summaryPurpose;
    private String governingAgency;
    private String department;
    private String userType;
    private String applicationDeadline;
}
