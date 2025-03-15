package com.hyetaekon.hyetaekon.user.repository;

import com.hyetaekon.hyetaekon.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
