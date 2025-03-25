package com.hyetaekon.hyetaekon.publicservice.repository;


import com.hyetaekon.hyetaekon.publicservice.entity.FamilyTypeEnum;
import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import com.hyetaekon.hyetaekon.publicservice.entity.ServiceCategory;
import com.hyetaekon.hyetaekon.publicservice.entity.SpecialGroupEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicServiceRepository extends JpaRepository<PublicService, Long> {
    Page<PublicService> findByServiceCategory(ServiceCategory category, Pageable pageable);

    List<PublicService> findTop6ByOrderByBookmarkCntDesc();

    Optional<PublicService> findById(long serviceId);

    int deleteByIdNotIn(List<Long> Ids);

    @Query("SELECT DISTINCT ps FROM PublicService ps " +
        "LEFT JOIN ps.specialGroups sg " +
        "LEFT JOIN ps.familyTypes ft " +
        "WHERE (:categories IS NULL OR :categories IS EMPTY OR ps.serviceCategory IN :categories) " +
        "AND (:specialGroupEnums IS NULL OR :specialGroupEnums IS EMPTY OR sg.specialGroupEnum IN :specialGroupEnums) " +
        "AND (:familyTypeEnums IS NULL OR :familyTypeEnums IS EMPTY OR ft.familyTypeEnum IN :familyTypeEnums)")
    Page<PublicService> findWithFilters(
        @Param("categories") List<ServiceCategory> categories,
        @Param("specialGroupEnums") List<SpecialGroupEnum> specialGroupEnums,
        @Param("familyTypeEnums") List<FamilyTypeEnum> familyTypeEnums,
        Pageable pageable
    );
}
