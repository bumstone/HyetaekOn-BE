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

    // 사용자의 북마크 공공서비스 목록 페이지
    @Query("SELECT p FROM PublicService p JOIN p.bookmarks b WHERE b.user.id = :userId ORDER BY b.createdAt DESC")
    Page<PublicService> findByBookmarks_User_Id(@Param("userId") Long userId, Pageable pageable);

}
