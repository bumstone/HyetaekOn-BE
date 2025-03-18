package com.hyetaekon.hyetaekon.user.repository;

import com.hyetaekon.hyetaekon.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
  // user id로 검색
  Optional<User> findByIdAndDeletedAtIsNull(Long id);

  // user email로 검색
  Optional<User> findByEmailAndDeletedAtIsNull(String email);

  // 이메일 또는 닉네임 중복 여부 확인
  @Query("SELECT u FROM User u WHERE (u.email = :email OR u.nickname = :nickname) AND u.deletedAt IS NULL")
  Optional<User> findByEmailOrNicknameAndDeletedAtIsNull(@Param("email") String email, @Param("nickname") String nickname);

  // 닉네임 중복 확인
  boolean existsByNickname(String nickname);

  // 이메일 중복 확인
  boolean existsByEmailAndDeletedAtIsNull(String email);

  // 정지된 회원 목록 조회
  @Query("SELECT u FROM User u WHERE u.suspendEndAt > :now AND u.deletedAt IS NULL")
  Page<User> findSuspendedUsers(Pageable pageable, @Param("now") LocalDateTime now);

  // 메서드 오버로딩: 현재 시간을 자동으로 설정
  default Page<User> findSuspendedUsers(Pageable pageable) {
    return findSuspendedUsers(pageable, LocalDateTime.now());
  }

  // 탈퇴한 회원 목록 조회
  @Query("SELECT u FROM User u WHERE u.deletedAt IS NOT NULL")
  Page<User> findWithdrawnUsers(Pageable pageable);
}
