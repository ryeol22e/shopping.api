package com.project.shopping.display.repository;

import static com.project.shopping.display.dto.QCategoryDTO.categoryDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.project.shopping.display.dto.CategoryDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
	@Qualifier(value = "mariadbFactory")
	private final JPAQueryFactory mariadbFactory;

	public List<CategoryDTO> findUpCateList(CategoryDTO dto) {
		return mariadbFactory.selectFrom(categoryDTO).where(
				dto.getUpCateNo() == null ? null : categoryDTO.upCateNo.eq(dto.getUpCateNo()),
				categoryDTO.useYn.eq(dto.getUseYn()), categoryDTO.dispYn.eq(dto.getDispYn()))
				.fetch();
	}
}
