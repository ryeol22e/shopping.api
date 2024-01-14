package com.project.shopping.utils;

import com.querydsl.core.types.dsl.BooleanExpression;

@FunctionalInterface
public interface QueryBool<T> {

	BooleanExpression getExpression(T t);
}
