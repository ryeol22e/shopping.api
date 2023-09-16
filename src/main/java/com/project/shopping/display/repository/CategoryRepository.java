package com.project.shopping.display.repository;

import static com.project.shopping.display.dto.QCategoryDTO.categoryDTO;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.project.shopping.display.dto.CategoryDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public List<CategoryDTO> findUpCateList(CategoryDTO dto) {
		return jpaQueryFactory.selectFrom(categoryDTO)
				.where(categoryDTO.upCateNo.eq(dto.getUpCateNo())
						.and(categoryDTO.useYn.eq(dto.getUseYn()))
						.and(categoryDTO.dispYn.eq(dto.getDispYn())))
				.fetch();
	}
}
