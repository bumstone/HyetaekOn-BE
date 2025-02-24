package com.hyetaekon.hyetaekon.publicservice.entity;

import com.hyetaekon.hyetaekon.publicservice.converter.BusinessTypeConverter;
import com.hyetaekon.hyetaekon.publicservice.converter.FamilyTypeConverter;
import com.hyetaekon.hyetaekon.publicservice.converter.OccupationConverter;
import com.hyetaekon.hyetaekon.publicservice.converter.SpecialGroupConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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

    @Column(nullable = false, length = 255)
    private String title;

    // TODO: 서비스 분야 - 카테고리 + 해시태그
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceCategory serviceCategory;

    @Column(name = "summary_purpose", columnDefinition = "TEXT")
    private String summaryPurpose;

    @Column(name = "service_purpose", nullable = false, columnDefinition = "TEXT")
    private String servicePurpose;

    @Column(name = "governing_agency", nullable = false, length = 100)
    private String governingAgency;

    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @Column(name = "user_type", nullable = false, length = 50)
    private String userType;


    // 지원 대상 필드
    @Column(name = "support_target", nullable = false, columnDefinition = "TEXT")
    private String supportTarget;

    @Column(name = "selection_criteria", nullable = false, columnDefinition = "TEXT")
    private String selectionCriteria;


    // 지원 관련 필드
    @Column(name = "support_details", nullable = false, columnDefinition = "TEXT")
    private String supportDetails;

    @Column(name = "support_type", nullable = false, length = 100)
    private String supportType;


    // 신청 내용 필드
    @Column(name = "application_method", nullable = false, columnDefinition = "TEXT")
    private String applicationMethod;

    @Column(name = "application_deadline", nullable = false, columnDefinition = "TEXT")
    private String applicationDeadline;


    // 추가정보 필드
    @Column(name = "required_documents", columnDefinition = "TEXT")
    private String requiredDocuments;

    @Column(name = "contact_info", length = 255)
    private String contactInfo;

    @Column(name = "online_application_url", columnDefinition = "TEXT")
    private String onlineApplicationUrl;

    @Column(name = "related_laws", columnDefinition = "TEXT")
    private String relatedLaws;


    // 지원조건 필드
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


    // 조회수
    @Builder.Default
    @Column(name = "views", nullable = false)
    private Integer views = 0;

    // 북마크수
    @Builder.Default
    @Column(name = "bookmark_cnt", nullable = false)
    private Integer bookmarkCnt = 0;


    // TODO: 특수 대상 그룹 - 검색 + 해시태그
    @Convert(converter = SpecialGroupConverter.class)  // 명시적 선언
    private SpecialGroup specialGroup;
    // TODO: 가구 형태 - 검색 + 해시태그
    @Convert(converter = FamilyTypeConverter.class)
    private FamilyType familyType;
    // TODO: 대상 직종 - 회원 정보
    @Convert(converter = OccupationConverter.class)
    private Occupation occupation;
    // TODO: 사업체 형태 - 회원 정보
    @Convert(converter = BusinessTypeConverter.class)
    private BusinessType businessType;


    public void updateBookmarkCntUp() {
        bookmarkCnt++;
    }

    public void updateBookmarkCntDown() {
        bookmarkCnt--;
    }

    public void updateViewsUp() { views++; }
}
