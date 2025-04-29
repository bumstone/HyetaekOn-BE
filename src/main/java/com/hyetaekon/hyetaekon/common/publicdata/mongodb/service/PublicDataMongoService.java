package com.hyetaekon.hyetaekon.common.publicdata.mongodb.service;


import com.hyetaekon.hyetaekon.common.publicdata.mongodb.document.PublicData;
import com.hyetaekon.hyetaekon.common.publicdata.mongodb.repository.PublicDataMongoRepository;
import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicDataMongoService {

    private final PublicDataMongoRepository mongoRepository;
    private final MongoTemplate mongoTemplate;

    public PublicData saveToMongo(PublicService publicService) {
        // 특수 그룹 정보 추출
        List<String> specialGroups = publicService.getSpecialGroups().stream()
            .map(sg -> sg.getSpecialGroupEnum().getType())
            .collect(Collectors.toList());

        // 가족 유형 정보 추출
        List<String> familyTypes = publicService.getFamilyTypes().stream()
            .map(ft -> ft.getFamilyTypeEnum().getType())
            .collect(Collectors.toList());

        // MongoDB 문서 생성
        PublicData document = PublicData.builder()
            .publicServiceId(publicService.getId())
            .serviceName(publicService.getServiceName())
            .summaryPurpose(publicService.getSummaryPurpose())
            .serviceCategory(publicService.getServiceCategory().getType())
            .specialGroup(specialGroups)
            .familyType(familyTypes)
            .build();

        return mongoRepository.save(document);
    }

    public List<PublicData> saveAllToMongo(List<PublicService> publicServices) {
        List<PublicData> documents = publicServices.stream()
            .map(this::convertToDocument)
            .collect(Collectors.toList());

        return mongoRepository.saveAll(documents);
    }

    private PublicData convertToDocument(PublicService publicService) {
        // 특수 그룹과 가족 유형 정보 추출
        List<String> specialGroups = publicService.getSpecialGroups().stream()
            .map(sg -> sg.getSpecialGroupEnum().getType())
            .collect(Collectors.toList());

        List<String> familyTypes = publicService.getFamilyTypes().stream()
            .map(ft -> ft.getFamilyTypeEnum().getType())
            .collect(Collectors.toList());

        // MongoDB 문서 생성
        return PublicData.builder()
            .publicServiceId(publicService.getId())
            .serviceName(publicService.getServiceName())
            .summaryPurpose(publicService.getSummaryPurpose())
            .serviceCategory(publicService.getServiceCategory().getType())
            .specialGroup(specialGroups)
            .familyType(familyTypes)
            .build();
    }
}
