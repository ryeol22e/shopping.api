package com.project.shopping.display.repository;

import static com.project.shopping.display.dto.QBannerDTO.bannerDTO;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.project.shopping.display.dto.BannerDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BannerRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public List<BannerDTO> findByBannerList(BannerDTO dto) {
		return jpaQueryFactory.selectFrom(bannerDTO)
				.where(bannerDTO.bannerType.eq(dto.getBannerType()),
						bannerDTO.useYn.eq(dto.getUseYn()), bannerDTO.dispYn.eq(dto.getDispYn()))
				.fetch();
	}

	public long save(BannerDTO dto) {
		return jpaQueryFactory.insert(bannerDTO).columns(bannerDTO).values(dto).execute();
	}
}
