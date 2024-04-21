package com.project.shopping.display.service;

import java.time.LocalDateTime;
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

	@Cacheable(value = "getBannerList", key = "#dto?.bannerType")
	public List<BannerInfo> getBannerList(BannerInfo dto) {
		LocalDateTime today = LocalDateTime.now();
		List<BannerInfo> list = bannerRepository.findByBannerList(dto).stream()
				.filter(x -> today.compareTo(x.getDispStartDtm()) >= 0
						&& today.compareTo(x.getDispEndDtm()) <= 0)
				.toList();

		return list;
	}
}
