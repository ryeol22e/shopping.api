package com.project.shopping.display.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.shopping.display.dto.BannerDTO;
import com.project.shopping.display.repository.BannerRepository;
import com.project.shopping.utils.UtilsData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DisplayService {
	private final BannerRepository bannerRepository;

	public List<BannerDTO> getBannerList(BannerDTO dto) throws Exception {
		LocalDateTime today = LocalDateTime.now();
		List<BannerDTO> list = bannerRepository.findByBannerTypeAndUseYnAndDispYn(dto.getBannerType(), dto.getUseYn(), dto.getDispYn()).stream()
		.filter(x-> today.compareTo(x.getDispStartDtm())>=0 && today.compareTo(x.getDispEndDtm())<=0).toList();
		
		for(int i=0, size=list.size() ; i<size ; i++) {
			BannerDTO banner = list.get(i);

			if(banner.getImageData()!=null) {
				banner.setImage(UtilsData.getBlobToByte(banner.getImageData()));
			}
		}

		return list;
	}
}
