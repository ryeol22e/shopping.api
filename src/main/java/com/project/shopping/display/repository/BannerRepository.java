package com.project.shopping.display.repository;

import static com.project.shopping.display.dto.QBannerTable.bannerTable;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.project.shopping.display.dto.BannerTable;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BannerRepository {
	@Qualifier(value = "mariadbFactory")
	private final JPAQueryFactory mariadbFactory;

	public List<BannerTable> findByBannerList(BannerTable dto) {
		return mariadbFactory.selectFrom(bannerTable)
				.where(bannerTable.bannerType.eq(dto.getBannerType()),
						bannerTable.useYn.eq(dto.getUseYn()), bannerTable.dispYn.eq(dto.getDispYn()))
				.fetch();
	}

	public long save(BannerTable dto) {
		return mariadbFactory.insert(bannerTable).columns(bannerTable).values(dto).execute();
	}
}
