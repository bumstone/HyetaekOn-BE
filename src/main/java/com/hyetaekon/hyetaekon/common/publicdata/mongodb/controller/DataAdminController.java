package com.hyetaekon.hyetaekon.common.publicdata.mongodb.controller;

import com.hyetaekon.hyetaekon.common.publicdata.mongodb.service.PublicDataMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/mongo")
@RequiredArgsConstructor
public class DataAdminController {
    private final PublicDataMongoService publicDataMongoService;

    @PostMapping("/data/deduplicate")
    public ResponseEntity<String> deduplicateMongo() {
        publicDataMongoService.deduplicateMongoDocuments();
        return ResponseEntity.ok("MongoDB 중복 데이터 정리가 시작되었습니다.");
    }
}
