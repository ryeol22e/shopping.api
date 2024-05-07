package com.project.shopping.aop;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import com.nhncorp.lucy.security.xss.XssPreventer;

/**
 * 
 * @author
 * @date 2024.05.07
 */
@Aspect
@Component
public class EscapeAOP {

	@Before("execution(* com.project.shopping.*.repository.*.insert*(..)) || execution(* com.project.shopping.*.repository.*.update*(..))")
	public void runEscape(JoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();

		for (int i = 0, length = args.length; i < length; i++) {
			Object arg = args[i];

			if (arg instanceof String) {
				escapeValue(arg);
			}
		}
	}

	private void escapeByList(List<?> list) throws Throwable {
		for (int i = 0, size = list.size(); i < size; i++) {
			escapeValue(list.get(i));
		}
	}

	private void escapeByList(Object param, Field field) throws Throwable {
		Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
		List<?> list = (List<?>) field.get(param);

		if (!type.getTypeName().equalsIgnoreCase("java.lang.String")) {
			escapeByList(list);
		} else {
			ReflectionUtils.setField(field, param, list.stream().map(item -> XssPreventer.escape((String) item)).toList());
		}
	}

	private void escapeValue(Object param) throws Throwable {
		Field[] fields = param.getClass().getDeclaredFields();

		for (int i = 0, length = fields.length; i < length; i++) {
			Field field = fields[i];
			Object value = null;

			ReflectionUtils.makeAccessible(field);
			value = field.get(param);

			if (value instanceof String) {
				ReflectionUtils.setField(field, param, XssPreventer.escape((String) value));
			}
			if (value instanceof List) {
				escapeByList(param, field);
			}

		}
	}
}
