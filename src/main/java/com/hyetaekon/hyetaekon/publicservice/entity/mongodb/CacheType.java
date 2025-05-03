package com.hyetaekon.hyetaekon.publicservice.entity.mongodb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    SERVICE_AUTOCOMPLETE("serviceAutocomplete", 2, 1200),   // 자동완성 캐시
    FILTER_OPTIONS("filterOptions", 24, 50),              // 필터 옵션 캐시 (하루 유지)
    MATCHED_SERVICES("matchedServices", 2, 100);          // 맞춤 서비스 캐시

    private final String cacheName;
    private final int expiredAfterWrite;  // 시간(hour) 단위
    private final int maximumSize;        // 최대 캐시 항목 수
}