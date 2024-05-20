package com.project.shopping.display.service;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.project.shopping.display.dto.BannerInfo;
import com.project.shopping.display.repository.BannerRepository;
import com.project.shopping.zconfig.annotations.CacheTime;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DisplayService {
    private final BannerRepository bannerRepository;

    @Cacheable(key = "#dto.bannerType + #dto.useYn + #dto.dispYn", value = "getBannerList", unless = "#result == null")
    @CacheTime(time = 24L)
    public List<BannerInfo> getBannerList(BannerInfo dto) {
        return bannerRepository.findByBannerList(dto);
    }
}
