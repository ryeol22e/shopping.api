package com.project.shopping.display.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.shopping.display.dto.BannerDTO;

@Repository
public interface BannerRepository extends JpaRepository<BannerDTO, Long> {
	public List<BannerDTO> findByBannerTypeAndUseYnAndDispYn(String bannerType, char useYn, char dispYn);
}
