package com.hyetaekon.hyetaekon.publicservice.repository;


import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import com.hyetaekon.hyetaekon.publicservice.entity.ServiceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicServiceRepository extends JpaRepository<PublicService, Long> {
    Page<PublicService> findByServiceCategory(ServiceCategory category, Pageable pageable);

    List<PublicService> findTop6ByOrderByViewsDesc();

    Optional<PublicService> findById(long serviceId);

    int deleteByServiceIdNotIn(List<Long> currentServiceIds);
}
