package com.project.shopping.product.repository;

import static com.project.shopping.product.dto.QProductInfo.productInfo;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.project.shopping.product.dto.ProductInfo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
	@Qualifier(value = "mariadbFactory")
	private final JPAQueryFactory mariadbFactory;

	public List<ProductInfo> findProductList(ProductInfo dto) {
		final long limit = 30;
		final Function<String, BooleanExpression> prdtNoGt = lastPrdtNo -> lastPrdtNo != null && !lastPrdtNo.isEmpty() && !lastPrdtNo.isBlank() ? productInfo.prdtNo.gt(lastPrdtNo) : null;

		return mariadbFactory
				.selectFrom(productInfo).where(
						productInfo.cateNo.eq(dto.getCateNo()),
						productInfo.useYn.eq(dto.getUseYn()),
						productInfo.dispYn.eq(dto.getDispYn()),
						prdtNoGt.apply(dto.getLastPrdtNo()))
				.orderBy(productInfo.prdtNo.asc())
				.limit(limit)
				.fetch();
	}

	public ProductInfo findProduct(String prdtNo) {
		return mariadbFactory.selectFrom(productInfo).where(productInfo.prdtNo.eq(prdtNo)).fetchOne();
	}

	public long save(ProductInfo dto) {
		return mariadbFactory.insert(productInfo)
				.columns(productInfo.cateNo, productInfo.prdtNo, productInfo.prdtName,
						productInfo.dispYn, productInfo.useYn, productInfo.normalPrice,
						productInfo.sellPrice, productInfo.imagePath, productInfo.imageName,
						productInfo.regDtime)
				.values(dto.getCateNo(), dto.getPrdtNo(), dto.getPrdtName(), dto.getDispYn(),
						dto.getUseYn(), dto.getNormalPrice(), dto.getSellPrice(),
						dto.getImagePath(), dto.getImageName(), dto.getRegDtime())
				.execute();
	}
}
