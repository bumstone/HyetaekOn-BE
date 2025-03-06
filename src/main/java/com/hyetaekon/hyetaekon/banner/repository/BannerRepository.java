package com.hyetaekon.hyetaekon.banner.repository;

import com.hyetaekon.hyetaekon.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
}
