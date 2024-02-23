package com.project.shopping.product.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import static com.project.shopping.product.dto.QProductTable.productTable;

public class ProductExpression {

	protected static BooleanExpression prdtNoLt(String lastPrdtNo) {
		return lastPrdtNo != null ? productTable.prdtNo.lt(lastPrdtNo) : null;
	}
}
