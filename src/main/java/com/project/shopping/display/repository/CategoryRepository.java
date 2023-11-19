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
	private final JPAQueryFactory factory;

	public List<CategoryDTO> findUpCateList(CategoryDTO dto) {
		return factory.selectFrom(categoryDTO)
				.where(dto.getUpCateNo()==null ? categoryDTO.upCateNo.isNull() : categoryDTO.upCateNo.eq(dto.getUpCateNo())
						.and(categoryDTO.useYn.eq(dto.getUseYn()))
						.and(categoryDTO.dispYn.eq(dto.getDispYn())))
				.fetch();
	}
}
