package com.project.shopping.product.repository;

import static com.project.shopping.product.dto.QProductDTO.productDTO;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Repository;
import com.project.shopping.product.dto.ProductDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
	private final JPAQueryFactory factory;

	public List<ProductDTO> findProductList(ProductDTO dto) {
		final long limit = 6;
		final Function<String, BooleanExpression> prdtNoGt = (lastPrdtNo) -> !lastPrdtNo.isEmpty() && !lastPrdtNo.isBlank() ? productDTO.prdtNo.gt(lastPrdtNo) : null;

		return factory
				.selectFrom(productDTO).where(
						productDTO.cateNo.eq(dto.getCateNo()),
						productDTO.useYn.eq(dto.getUseYn()),
						productDTO.dispYn.eq(dto.getDispYn()),
						prdtNoGt.apply(dto.getLastPrdtNo()))
				.orderBy(productDTO.prdtNo.asc())
				.limit(limit)
				.fetch();
	}

	public ProductDTO findProduct(String prdtNo) {
		return factory.selectFrom(productDTO).where(productDTO.prdtNo.eq(prdtNo)).fetchOne();
	}

	public long save(ProductDTO dto) {
		return factory.insert(productDTO)
				.columns(productDTO.cateNo, productDTO.prdtNo, productDTO.prdtName,
						productDTO.dispYn, productDTO.useYn, productDTO.normalPrice,
						productDTO.sellPrice, productDTO.imagePath, productDTO.imageName,
						productDTO.regDtime)
				.values(dto.getCateNo(), dto.getPrdtNo(), dto.getPrdtName(), dto.getDispYn(),
						dto.getUseYn(), dto.getNormalPrice(), dto.getSellPrice(),
						dto.getImagePath(), dto.getImageName(), dto.getRegDtime())
				.execute();
	}
}
