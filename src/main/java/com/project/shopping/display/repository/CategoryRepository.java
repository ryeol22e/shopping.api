package com.project.shopping.display.repository;

import static com.project.shopping.display.dto.QStandardCategory.standardCategory;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.project.shopping.display.dto.StandardCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
	@Qualifier(value = "mariadbFactory")
	private final JPAQueryFactory mariadbFactory;

	public List<StandardCategory> findUpCateList(StandardCategory dto) {
		return mariadbFactory.selectFrom(standardCategory).where(
				dto.getUpCateNo() == null ? null : standardCategory.upCateNo.eq(dto.getUpCateNo()),
				standardCategory.useYn.eq(dto.getUseYn()), standardCategory.dispYn.eq(dto.getDispYn()))
				.fetch();
	}
}
