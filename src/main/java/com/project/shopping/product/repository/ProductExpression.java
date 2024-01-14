package com.project.shopping.product.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import static com.project.shopping.product.dto.QProductDTO.productDTO;

public class ProductExpression {

	protected static BooleanExpression prdtNoLt(String lastPrdtNo) {
		return lastPrdtNo != null ? productDTO.prdtNo.lt(lastPrdtNo) : null;
	}
}
