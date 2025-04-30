package com.hyetaekon.hyetaekon.common.publicdata.mongodb.service;

import com.hyetaekon.hyetaekon.common.publicdata.mongodb.document.PublicData;
import com.hyetaekon.hyetaekon.common.publicdata.mongodb.repository.PublicDataMongoRepository;
import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicDataMongoService {

    private final PublicDataMongoRepository mongoRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * 단일 공공서비스 엔티티를 MongoDB에 저장
     */
    public PublicData saveToMongo(PublicService publicService) {
        PublicData document = convertToDocument(publicService);
        return mongoRepository.save(document);
    }

    /**
     * 공공서비스 엔티티를 MongoDB 문서로 변환
     */
    private PublicData convertToDocument(PublicService publicService) {
        // 특수 그룹 정보 추출
        List<String> specialGroups = publicService.getSpecialGroups().stream()
            .map(sg -> sg.getSpecialGroupEnum().getType())
            .collect(Collectors.toList());

        // 가족 유형 정보 추출
        List<String> familyTypes = publicService.getFamilyTypes().stream()
            .map(ft -> ft.getFamilyTypeEnum().getType())
            .collect(Collectors.toList());

        // 직업 정보 추출
        List<String> occupations = publicService.getOccupations().stream()
            .map(occ -> occ.getOccupationEnum().getType())
            .collect(Collectors.toList());

        // 사업체 유형 정보 추출
        List<String> businessTypes = publicService.getBusinessTypes().stream()
            .map(bt -> bt.getBusinessTypeEnum().getType())
            .collect(Collectors.toList());

        // MongoDB 문서 생성 및 반환
        return PublicData.builder()
            .publicServiceId(publicService.getId())
            .serviceName(publicService.getServiceName())
            .summaryPurpose(publicService.getSummaryPurpose())
            .serviceCategory(publicService.getServiceCategory().getType())
            .specialGroup(specialGroups)
            .familyType(familyTypes)
            .occupations(occupations)
            .businessTypes(businessTypes)
            .targetGenderMale(publicService.getTargetGenderMale())
            .targetGenderFemale(publicService.getTargetGenderFemale())
            .targetAgeStart(publicService.getTargetAgeStart())
            .targetAgeEnd(publicService.getTargetAgeEnd())
            .incomeLevel(publicService.getIncomeLevel())
            .build();
    }

    /**
     * 서비스 ID로 문서 조회
     */
    public Optional<PublicData> findByPublicServiceId(String publicServiceId) {
        return mongoRepository.findByPublicServiceId(publicServiceId);
    }

    /**
     * 기존 문서 업데이트 또는 새 문서 생성
     */
    public PublicData updateOrCreateDocument(PublicService publicService) {
        Optional<PublicData> existingDoc = mongoRepository.findByPublicServiceId(publicService.getId());

        if (existingDoc.isPresent()) {
            // 기존 문서의 ID 유지하면서 데이터 업데이트
            PublicData newData = convertToDocument(publicService);
            newData.setId(existingDoc.get().getId());
            return mongoRepository.save(newData);
        } else {
            // 새 문서 생성
            return saveToMongo(publicService);
        }
    }

    /**
     * 여러 서비스 문서 업데이트 또는 생성
     */
    public void updateOrCreateBulkDocuments(List<PublicService> services) {
        // 모든 service ID 목록
        List<String> serviceIds = services.stream()
            .map(PublicService::getId)
            .collect(Collectors.toList());

        // publicServiceId로 기존 문서 조회 (중요: findAllByPublicServiceId를 사용)
        Map<String, PublicData> existingDocsMap = mongoRepository.findAllByPublicServiceIdIn(serviceIds).stream()
            .collect(Collectors.toMap(
                PublicData::getPublicServiceId,
                doc -> doc,
                (a, b) -> a  // 중복 시 첫 번째 문서 유지
            ));

        // 처리할 문서 준비
        List<PublicData> docsToSave = services.stream()
            .map(service -> {
                PublicData doc = convertToDocument(service);
                if (existingDocsMap.containsKey(service.getId())) {
                    // 기존 문서의 ID 유지
                    doc.setId(existingDocsMap.get(service.getId()).getId());
                }
                return doc;
            })
            .collect(Collectors.toList());

        // 저장
        mongoRepository.saveAll(docsToSave);
    }

    @PostConstruct
    public void ensureIndexes() {
        mongoTemplate.indexOps("service_info").ensureIndex(
            new Index().on("publicServiceId", Sort.Direction.ASC).unique()
        );
        log.info("MongoDB 인덱스 설정 완료: publicServiceId (unique)");
    }

    @Transactional
    public void deduplicateMongoDocuments() {
        log.info("MongoDB 문서 중복 제거 시작");

        // 1. 모든 publicServiceId 목록 조회
        List<String> allServiceIds = mongoRepository.findAllPublicServiceIds();
        int totalProcessed = 0;
        int totalRemoved = 0;

        // 2. 각 ID별로 처리
        for (String serviceId : allServiceIds) {
            List<PublicData> duplicates = mongoRepository.findAllByPublicServiceId(serviceId);

            if (duplicates.size() > 1) {
                // 가장 최근 문서를 남기고 나머지 삭제
                PublicData latestDoc = duplicates.get(0); // 또는 타임스탬프로 정렬하여 최신 문서 선택

                // 나머지 중복 문서 삭제
                for (int i = 1; i < duplicates.size(); i++) {
                    mongoRepository.delete(duplicates.get(i));
                    totalRemoved++;
                }
            }

            totalProcessed++;
            if (totalProcessed % 1000 == 0) {
                log.info("중복 제거 진행 중: {}/{} 처리, {}개 제거됨",
                    totalProcessed, allServiceIds.size(), totalRemoved);
            }
        }

        log.info("MongoDB 문서 중복 제거 완료: 총 {}개 중 {}개 중복 제거됨",
            totalProcessed, totalRemoved);
    }
}