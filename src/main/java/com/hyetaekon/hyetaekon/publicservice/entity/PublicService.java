package com.hyetaekon.hyetaekon.publicservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "public_service")
public class PublicService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", nullable = false, length = 255)
    private String serviceName;  // 서비스명

    // TODO: 서비스 분야 - 카테고리 + 해시태그
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceCategory serviceCategory;   // 서비스 분야

    @Column(name = "summary_purpose", columnDefinition = "TEXT")
    private String summaryPurpose;  // 서비스 목적 요약

    @Column(name = "governing_agency", nullable = false, length = 100)
    private String governingAgency;  // 소관기관명

    @Column(name = "department", nullable = false, length = 100)
    private String department;  // 부서명

    @Column(name = "user_type", nullable = false, length = 50)
    private String userType;  // 사용자 구분


    // 지원 대상 필드
    @Column(name = "support_target", nullable = false, columnDefinition = "TEXT")
    private String supportTarget;  // 지원 대상

    @Column(name = "selection_criteria", nullable = false, columnDefinition = "TEXT")
    private String selectionCriteria;  // 선정 기준


    // 지원 관련 필드
    @Column(name = "service_purpose", nullable = false, columnDefinition = "TEXT")
    private String servicePurpose;  // 서비스 목적

    @Column(name = "support_details", nullable = false, columnDefinition = "TEXT")
    private String supportDetails;  // 지원 내용

    @Column(name = "support_type", nullable = false, length = 100)
    private String supportType;  // 지원 유형


    // 신청 내용 필드
    @Column(name = "application_method", nullable = false, columnDefinition = "TEXT")
    private String applicationMethod;  // 신청 방법(상세)

    @Column(name = "application_deadline", nullable = false, columnDefinition = "TEXT")
    private String applicationDeadline;  // 신청 기한(상세)


    // 추가정보 필드
    @Column(name = "required_documents", columnDefinition = "TEXT")
    private String requiredDocuments;  // 구비 서류

    @Column(name = "contact_info", length = 255)
    private String contactInfo;  // 문의처

    @Column(name = "online_application_url", columnDefinition = "TEXT")
    private String onlineApplicationUrl;  // 온라인 경로 url

    @Column(name = "related_laws", columnDefinition = "TEXT")
    private String relatedLaws;  // 관련 법률


    // 지원조건 필드 - 유저 정보 비교용
    @Column(name = "target_gender_male", nullable = false)
    private boolean targetGenderMale;

    @Column(name = "target_gender_Female", nullable = false)
    private boolean targetGenderFemale;

    @Column(name = "target_age_start")
    private Integer targetAgeStart;

    @Column(name = "target_age_end")
    private Integer targetAgeEnd;

    @Column(name = "income_level", length = 255)
    private String incomeLevel;

    /*@Column(name = "income_level_very_low")
    private boolean incomeLevelVeryLow; // 중위소득 0~50%
    @Column(name = "income_level_low")
    private boolean incomeLevelLow; // 중위소득 51~75%
    @Column(name = "income_level_medium")
    private boolean incomeLevelMedium; // 중위소득 76~100%
    @Column(name = "income_level_high")
    private boolean incomeLevelHigh; // 중위소득 101~200%
    @Column(name = "income_level_very_high")
    private boolean incomeLevelVeryHigh; // 중위소득 200% 초과*/

    // 조회수
    @Builder.Default
    @Column(name = "views", nullable = false)
    private Integer views = 0;

    // 북마크수
    @Builder.Default
    @Column(name = "bookmark_cnt", nullable = false)
    private Integer bookmarkCnt = 0;


    @OneToMany(mappedBy = "publicService",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    private List<SpecialGroup> specialGroups = new ArrayList<>();

    @OneToMany(mappedBy = "publicService",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    private List<FamilyType> familyTypes = new ArrayList<>();

    @OneToMany(mappedBy = "publicService",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    private List<Occupation> occupations = new ArrayList<>();

    @OneToMany(mappedBy = "publicService",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    private List<BusinessType> businessTypes = new ArrayList<>();


    public void updateBookmarkCntUp() {
        bookmarkCnt++;
    }

    public void updateBookmarkCntDown() {
        bookmarkCnt--;
    }

    public void updateViewsUp() { views++; }
}
