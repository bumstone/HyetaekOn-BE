package com.hyetaekon.hyetaekon.common.publicdata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicServiceDetailDataParsedResponseDto {
    private long serviceId;
    private String servicePurpose;
    private String supportTarget;
    private String selectionCriteria;
    private String supportDetail;
    private String supportType;
    private String applicationMethod;
    private String requiredDocuments;
    private String contactInfo;
    private String onlineApplicationUrl;
    private String relatedLaws;
}
