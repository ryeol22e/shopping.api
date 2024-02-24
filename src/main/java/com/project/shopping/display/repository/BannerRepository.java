package com.project.shopping.display.repository;

import static com.project.shopping.display.dto.QBannerInfo.bannerInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.project.shopping.display.dto.BannerInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BannerRepository {
	@Qualifier(value = "mariadbFactory")
	private final JPAQueryFactory mariadbFactory;

	public List<BannerInfo> findByBannerList(BannerInfo dto) {
		return mariadbFactory.selectFrom(bannerInfo)
				.where(bannerInfo.bannerType.eq(dto.getBannerType()),
						bannerInfo.useYn.eq(dto.getUseYn()), bannerInfo.dispYn.eq(dto.getDispYn()))
				.fetch();
	}

	public long save(BannerInfo dto) {
		return mariadbFactory.insert(bannerInfo).columns(bannerInfo).values(dto).execute();
	}
}
