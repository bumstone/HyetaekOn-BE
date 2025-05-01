package com.hyetaekon.hyetaekon.userInterest.repository;

import com.hyetaekon.hyetaekon.userInterest.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
    /**
     * 사용자 ID에 해당하는 모든 관심사 항목을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자의 관심사 목록
     */
    List<UserInterest> findByUserId(Long userId);

    /**
     * 사용자 ID로 관심사 존재 여부 확인
     *
     * @param userId 사용자 ID
     * @return 관심사 존재 여부
     */
    boolean existsByUserId(Long userId);

}
