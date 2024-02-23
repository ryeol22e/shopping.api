package com.project.shopping.product.repository;

import static com.project.shopping.product.dto.QProductTable.productTable;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.project.shopping.product.dto.ProductTable;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
	@Qualifier(value = "mariadbFactory")
	private final JPAQueryFactory mariadbFactory;

	public List<ProductTable> findProductList(ProductTable dto) {
		final long limit = 6;
		final Function<String, BooleanExpression> prdtNoGt = (lastPrdtNo) -> !lastPrdtNo.isEmpty() && !lastPrdtNo.isBlank() ? productTable.prdtNo.gt(lastPrdtNo) : null;

		return mariadbFactory
				.selectFrom(productTable).where(
						productTable.cateNo.eq(dto.getCateNo()),
						productTable.useYn.eq(dto.getUseYn()),
						productTable.dispYn.eq(dto.getDispYn()),
						prdtNoGt.apply(dto.getLastPrdtNo()))
				.orderBy(productTable.prdtNo.asc())
				.limit(limit)
				.fetch();
	}

	public ProductTable findProduct(String prdtNo) {
		return mariadbFactory.selectFrom(productTable).where(productTable.prdtNo.eq(prdtNo)).fetchOne();
	}

	public long save(ProductTable dto) {
		return mariadbFactory.insert(productTable)
				.columns(productTable.cateNo, productTable.prdtNo, productTable.prdtName,
						productTable.dispYn, productTable.useYn, productTable.normalPrice,
						productTable.sellPrice, productTable.imagePath, productTable.imageName,
						productTable.regDtime)
				.values(dto.getCateNo(), dto.getPrdtNo(), dto.getPrdtName(), dto.getDispYn(),
						dto.getUseYn(), dto.getNormalPrice(), dto.getSellPrice(),
						dto.getImagePath(), dto.getImageName(), dto.getRegDtime())
				.execute();
	}
}
