package com.hyetaekon.hyetaekon.common.publicdata.mongodb.service;

import com.hyetaekon.hyetaekon.common.publicdata.mongodb.document.PublicData;
import com.hyetaekon.hyetaekon.common.publicdata.mongodb.repository.PublicDataMongoRepository;
import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

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
     * 여러 공공서비스 엔티티를 MongoDB에 저장
     */
    public List<PublicData> saveAllToMongo(List<PublicService> publicServices) {
        List<PublicData> documents = publicServices.stream()
            .map(this::convertToDocument)
            .collect(Collectors.toList());

        return mongoRepository.saveAll(documents);
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
    public List<PublicData> updateOrCreateBulkDocuments(List<PublicService> services) {
        // 기존 ID 목록 가져오기
        List<String> serviceIds = services.stream()
            .map(PublicService::getId)
            .collect(Collectors.toList());

        // ID에 해당하는 문서 맵 생성
        Map<String, PublicData> existingDocsMap = mongoRepository.findAllById(serviceIds).stream()
            .collect(Collectors.toMap(PublicData::getPublicServiceId, doc -> doc, (a, b) -> a));

        // 각 서비스 처리
        List<PublicData> docsToSave = services.stream()
            .map(service -> {
                PublicData doc = convertToDocument(service);
                if (existingDocsMap.containsKey(service.getId())) {
                    // 기존 문서 ID 유지
                    doc.setId(existingDocsMap.get(service.getId()).getId());
                }
                return doc;
            })
            .collect(Collectors.toList());

        // 일괄 저장
        return mongoRepository.saveAll(docsToSave);
    }
}