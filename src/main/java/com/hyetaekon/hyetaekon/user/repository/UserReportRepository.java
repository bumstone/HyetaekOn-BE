package com.hyetaekon.hyetaekon.user.repository;


import com.hyetaekon.hyetaekon.user.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    // 기본 CRUD 메서드 사용
}
