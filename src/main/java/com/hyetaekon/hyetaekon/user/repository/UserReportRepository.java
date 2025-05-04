package com.hyetaekon.hyetaekon.user.repository;


import com.hyetaekon.hyetaekon.user.entity.ReportStatus;
import com.hyetaekon.hyetaekon.user.entity.UserReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    // 상태별 신고 목록 조회
    Page<UserReport> findByStatus(ReportStatus status, Pageable pageable);

    // 특정 사용자가 신고한 내역 조회
    Page<UserReport> findByReporter_Id(Long reporterId, Pageable pageable);

    // 특정 사용자가 신고당한 내역 조회
    Page<UserReport> findByReported_Id(Long reportedId, Pageable pageable);

    // 신고 처리되지 않은 건수 확인
    long countByStatus(ReportStatus status);
}
