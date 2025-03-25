package com.hyetaekon.hyetaekon.publicservice.repository;


import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import com.hyetaekon.hyetaekon.publicservice.entity.ServiceCategory;
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
        "AND (:specialGroupCodes IS NULL OR :specialGroupCodes IS EMPTY OR sg.code IN :specialGroupCodes) " +
        "AND (:familyTypeCodes IS NULL OR :familyTypeCodes IS EMPTY OR ft.code IN :familyTypeCodes)")
    Page<PublicService> findWithFilters(
        @Param("categories") List<ServiceCategory> categories,
        @Param("specialGroupCodes") List<String> specialGroupCodes,
        @Param("familyTypeCodes") List<String> familyTypeCodes,
        Pageable pageable
    );
}
