package com.project.shopping.display.repository;

import static com.project.shopping.display.dto.QCategoryInfo.categoryInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.project.shopping.display.dto.CategoryInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
    @Qualifier(value = "mariadbFactory")
    private final JPAQueryFactory mariadbFactory;

    public List<CategoryInfo> findUpCateList(CategoryInfo dto) {
        return mariadbFactory.selectFrom(categoryInfo).where(
                dto.getUpCateNo() == null ? null : categoryInfo.upCateNo.eq(dto.getUpCateNo()),
                categoryInfo.useYn.eq(dto.getUseYn()), categoryInfo.dispYn.eq(dto.getDispYn()))
                .fetch();
    }
}
