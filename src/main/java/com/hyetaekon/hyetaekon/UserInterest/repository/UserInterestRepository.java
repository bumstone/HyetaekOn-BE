package com.hyetaekon.hyetaekon.UserInterest.repository;

import com.hyetaekon.hyetaekon.UserInterest.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

}
