package com.hyetaekon.hyetaekon.publicservice.repository.mongodb;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.hyetaekon.hyetaekon.publicservice.dto.mongodb.ServiceSearchCriteriaDto;
import com.hyetaekon.hyetaekon.publicservice.dto.mongodb.ServiceSearchResultDto;
import com.hyetaekon.hyetaekon.publicservice.entity.mongodb.ServiceInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ServiceSearchClient {
    private final MongoTemplate mongoTemplate;
    private static final String SEARCH_INDEX = "searchIndex";
    private static final String AUTOCOMPLETE_INDEX = "serviceAutocompleteIndex";
    private static final String COLLECTION_NAME = "service_info";

    private static final String PROJECT_STAGE = """
        {
            $project: {
                publicServiceId: 1,
                serviceName: 1,
                summaryPurpose: 1,
                serviceCategory: 1,
                specialGroup: 1,
                familyType: 1,
                occupations: 1,
                businessTypes: 1,
                targetGenderMale: 1,
                targetGenderFemale: 1,
                targetAgeStart: 1,
                targetAgeEnd: 1,
                incomeLevel: 1,
                score: {$meta: 'searchScore'}
            }
        }""";

    public ServiceSearchResultDto search(ServiceSearchCriteriaDto criteria) {
        List<AggregationOperation> operations = new ArrayList<>();

        // 검색 쿼리 추가
        operations.add(context -> Document.parse(buildSearchQuery(criteria)));

        // 프로젝션 추가
        operations.add(context -> Document.parse(PROJECT_STAGE));

        // 페이징 처리
        operations.add(context -> Document.parse(buildFacetStage(criteria.getPageable())));

        AggregationResults<Document> results = mongoTemplate.aggregate(
            Aggregation.newAggregation(operations),
            COLLECTION_NAME,
            Document.class
        );

        return processResults(results, criteria.getPageable());
    }

    private String buildSearchQuery(ServiceSearchCriteriaDto criteria) {
        List<String> shouldClauses = new ArrayList<>();
        String mustClause = buildUserMustClause(criteria);

        // 검색어 관련 조건 추가
        if (StringUtils.hasText(criteria.getSearchTerm())) {
            addSearchTermClauses(shouldClauses, criteria.getSearchTerm());
        }

        // 사용자 관심사 관련 조건 추가
        if (criteria.getUserInterests() != null && !criteria.getUserInterests().isEmpty()) {
            for (String interest : criteria.getUserInterests()) {
                shouldClauses.add(createSearchClause("serviceCategory", interest, 2.5f, 0));
                shouldClauses.add(createSearchClause("specialGroup", interest, 2.5f, 0));
                shouldClauses.add(createSearchClause("familyType", interest, 2.5f, 0));
            }
        }

        // 소득 수준 일치 가산점 (should 조건)
        addUserMatchBoosts(shouldClauses, criteria);

        if (StringUtils.hasText(criteria.getUserIncomeLevel())) {
            shouldClauses.add(createSearchClause("incomeLevel", criteria.getUserIncomeLevel(), 2.8f, 0));
            shouldClauses.add(createSearchClause("incomeLevel", "ANY", 1.0f, 0));
        }

        String shouldClausesStr = shouldClauses.isEmpty() ? "[]" : "[" + String.join(",", shouldClauses) + "]";

        // must 조건이 있으면 추가, 없으면 생략
        String compoundQuery = """
        compound: {
            should: %s%s
        }
    """.formatted(shouldClausesStr, mustClause);

        return """
        {
            $search: {
                index: '%s',
                %s
            }
        }""".formatted(SEARCH_INDEX, compoundQuery);
    }

    private void addSearchTermClauses(List<String> clauses, String searchTerm) {
        // 서비스명 검색
        clauses.add(createSearchClause("serviceName", searchTerm, 5.0f, 1));
        clauses.add(createSearchClause("serviceName", searchTerm, 4.5f, 2));

        // 요약 검색
        clauses.add(createSearchClause("summaryPurpose", searchTerm, 3.5f, 0));

        // 서비스 분야 검색
        clauses.add(createSearchClause("serviceCategory", searchTerm, 4.5f, 1));

        // 특수그룹 검색
        clauses.add(createSearchClause("specialGroup", searchTerm, 4.0f, 1));

        // 가족유형 검색
        clauses.add(createSearchClause("familyType", searchTerm, 4.0f, 1));

        // 직업 검색
        clauses.add(createSearchClause("occupations", searchTerm, 3.0f, 0));

        // 사업자 유형 검색
        clauses.add(createSearchClause("businessTypes", searchTerm, 3.0f, 0));

        // 정규식 전방 일치 검색 (서비스명)
        clauses.add("""
            {regex: {
                query: '%s.*',
                path: 'serviceName',
                allowAnalyzedField: true,
                score: {boost: {value: 4.0}}
            }}""".formatted(searchTerm));
    }

    private String buildUserMustClause(ServiceSearchCriteriaDto criteria) {
        List<String> mustClauses = new ArrayList<>();

        // 성별 필수 조건
        if (StringUtils.hasText(criteria.getUserGender())) {
            String genderField = "MALE".equalsIgnoreCase(criteria.getUserGender())
                ? "targetGenderMale" : "targetGenderFemale";

            // 대상 성별이 null이거나 Y인 서비스만 포함
            mustClauses.add("""
            {
                compound: {
                    should: [
                        {exists: {path: "%s", exists: false}},
                        {equals: {path: "%s", value: "Y"}}
                    ]
                }
            }""".formatted(genderField, genderField));
        }

        // 나이 필수 조건
        if (criteria.getUserAge() != null) {
            int age = criteria.getUserAge();

            // 대상 나이 범위가 null이거나 사용자 나이를 포함하는 서비스만 포함
            mustClauses.add("""
            {
                compound: {
                    should: [
                        {exists: {path: "targetAgeStart", exists: false}},
                        {range: {path: "targetAgeStart", lte: %d}}
                    ]
                }
            }""".formatted(age));

            mustClauses.add("""
            {
                compound: {
                    should: [
                        {exists: {path: "targetAgeEnd", exists: false}},
                        {range: {path: "targetAgeEnd", gte: %d}}
                    ]
                }
            }""".formatted(age));
        }

        // must 조건이 없으면 빈 문자열 반환
        if (mustClauses.isEmpty()) {
            return "";
        }

        // must 조건이 있으면 문자열 형식으로 반환
        return ", must: [" + String.join(",", mustClauses) + "]";
    }

    private void addUserMatchBoosts(List<String> clauses, ServiceSearchCriteriaDto criteria) {
        // 직업 일치 가산점
        if (StringUtils.hasText(criteria.getUserJob())) {
            String userJob = criteria.getUserJob();

            // Occupation 필드와 일치 시 가산점
            clauses.add(createSearchClause("occupations", userJob, 3.0f, 0));

            // BusinessType 필드와 일치 시 가산점
            clauses.add(createSearchClause("businessTypes", userJob, 3.0f, 0));
        }

        // 소득 수준 일치 가산점
        if (StringUtils.hasText(criteria.getUserIncomeLevel())) {
            String userIncomeLevel = criteria.getUserIncomeLevel();

            // 1. 정확히 일치하는 소득수준에 높은 가산점
            clauses.add("""
            {text: {
                query: '%s',
                path: 'incomeLevel',
                score: {boost: {value: 2.8}}
            }}""".formatted(userIncomeLevel));

            // 2. ANY 값은 모든 소득수준에 매칭 가능
            clauses.add("""
            {text: {
                query: 'ANY',
                path: 'incomeLevel',
                score: {boost: {value: 1.0}}
            }}""");

            // 3. 사용자 소득수준보다 낮은 범위도 포함 (범위별 가산점)
            addIncomeLevelRangeBoosts(clauses, userIncomeLevel);
        }
    }

    // 소득수준 범위에 따른 가산점 추가
    private void addIncomeLevelRangeBoosts(List<String> clauses, String userIncomeLevel) {
        switch (userIncomeLevel) {
            case "HIGH":
                clauses.add(createSearchClause("incomeLevel", "MIDDLE_HIGH", 2.5f, 0));
                clauses.add(createSearchClause("incomeLevel", "MIDDLE", 2.0f, 0));
                clauses.add(createSearchClause("incomeLevel", "MIDDLE_LOW", 1.5f, 0));
                clauses.add(createSearchClause("incomeLevel", "LOW", 1.0f, 0));
                break;
            case "MIDDLE_HIGH":
                clauses.add(createSearchClause("incomeLevel", "MIDDLE", 2.5f, 0));
                clauses.add(createSearchClause("incomeLevel", "MIDDLE_LOW", 2.0f, 0));
                clauses.add(createSearchClause("incomeLevel", "LOW", 1.5f, 0));
                break;
            case "MIDDLE":
                clauses.add(createSearchClause("incomeLevel", "MIDDLE_LOW", 2.0f, 0));
                clauses.add(createSearchClause("incomeLevel", "LOW", 1.5f, 0));
                break;
            case "MIDDLE_LOW":
                clauses.add(createSearchClause("incomeLevel", "LOW", 2.0f, 0));
                break;
            default:
                break;
        }
    }

    private String createSearchClause(String path, String query, float boost, int maxEdits) {
        return """
            {text: {
                query: '%s',
                path: '%s',
                score: {boost: {value: %.1f}}%s
            }}""".formatted(
            query,
            path,
            boost,
            maxEdits > 0 ? ", fuzzy: {maxEdits: " + maxEdits + "}" : ""
        );
    }

    private String buildFacetStage(Pageable pageable) {
        return """
            {
                $facet: {
                    results: [{$skip: %d}, {$limit: %d}],
                    total: [{$count: 'count'}]
                }
            }""".formatted(pageable.getOffset(), pageable.getPageSize());
    }

    private ServiceSearchResultDto processResults(AggregationResults<Document> results, Pageable pageable) {
        Document result = results.getUniqueMappedResult();
        if (result == null) {
            return ServiceSearchResultDto.of(List.of(), 0L, pageable);
        }

        List<Document> resultDocs = result.get("results", List.class);
        List<Document> totalDocs = result.get("total", List.class);

        if (resultDocs == null) {
            return ServiceSearchResultDto.of(List.of(), 0L, pageable);
        }

        List<ServiceInfo> searchResults = resultDocs.stream()
            .map(doc -> mongoTemplate.getConverter().read(ServiceInfo.class, doc))
            .toList();

        long total = 0L;
        if (totalDocs != null && !totalDocs.isEmpty()) {
            Number count = totalDocs.getFirst().get("count", Number.class);
            total = count != null ? count.longValue() : 0L;
        }

        return ServiceSearchResultDto.of(searchResults, total, pageable);
    }

    // 검색어 자동완성
    public List<String> getAutocompleteResults(String word) {
        if (!StringUtils.hasText(word) || word.length() < 2) {
            return new ArrayList<>();
        }

        return mongoTemplate.aggregate(
                Aggregation.newAggregation(
                    context -> Document.parse("""
                    {
                        $search: {
                            index: '%s',
                            autocomplete: {
                                query: '%s',
                                path: 'serviceName',
                                fuzzy: {maxEdits: 1}
                            }
                        }
                    }""".formatted(AUTOCOMPLETE_INDEX, word)),
                    Aggregation.project("serviceName"),
                    Aggregation.limit(8)
                ),
                COLLECTION_NAME,
                Document.class
            )
            .getMappedResults()
            .stream()
            .map(doc -> doc.getString("serviceName"))
            .distinct()
            .collect(Collectors.toList());
    }
}
