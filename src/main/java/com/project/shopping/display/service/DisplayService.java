package com.project.shopping.display.service;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.project.shopping.display.dto.BannerInfo;
import com.project.shopping.display.repository.BannerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DisplayService {
	private final BannerRepository bannerRepository;

	@Cacheable(key = "#dto.bannerType + #dto.useYn + #dto.dispYn", value = "getBannerList")
	public List<BannerInfo> getBannerList(BannerInfo dto) {
		return bannerRepository.findByBannerList(dto);
	}
}
