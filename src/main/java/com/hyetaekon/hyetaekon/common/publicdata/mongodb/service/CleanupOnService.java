package com.hyetaekon.hyetaekon.common.publicdata.mongodb.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CleanupOnService {
    private final PublicDataMongoService publicDataMongoService;

    @PostConstruct
    public void cleanupOnStartup() {
        // 서버 시작 시 중복 데이터 정리 실행
        publicDataMongoService.deduplicateMongoDocuments();
    }
}
