package com.project.shopping.aop;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.nhncorp.lucy.security.xss.XssPreventer;
import com.project.shopping.ShoppingApplication;
import com.project.shopping.utils.ListSearch;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author
 * @date 2024.05.07
 */
@Slf4j
@Aspect
@Component
public class XssFilterAOP {
	private static final String TYPE_STRING = "string";
	private static final String TYPE_ARRAY = "[]";
	// 프로젝트 root packageName 작성
	private static final String PROJECT_PATH = ShoppingApplication.class.getPackageName();

	@Around("execution(* com.project.shopping.*.controller.*Controller.*(..))")
	public Object executeXssFilter(ProceedingJoinPoint joinPoint) throws Throwable {
		final String[] ignorePathArray = {};
		long start = System.nanoTime();
		Object returnValue = joinPoint.proceed();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		if (!ListSearch.isExistItem(Arrays.asList(ignorePathArray), request.getRequestURI())) {
			returnValue = afterRunMethod(returnValue);
		}

		long end = System.nanoTime();
		log.info("convert xss filter time : {}sec", String.format("%.2f", ((double) end - start) / 1000000000));

		return returnValue;
	}

	/**
	 * 후처리
	 *
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private Object afterRunMethod(Object value) throws Exception {
		if (value != null) {
			String pkgPath = value.getClass().getPackageName();
			String typeName = value.getClass().getSimpleName();

			if (TYPE_STRING.equalsIgnoreCase(typeName) || (TYPE_STRING.concat(TYPE_ARRAY)).equalsIgnoreCase(typeName)) {
				value = typeName.contains(TYPE_ARRAY) ? convertStringArray((String[]) value, true) : convertString((String) value, true);
			}

			if (!(value instanceof Collection) && pkgPath.contains(PROJECT_PATH)) {
				initDtoInField(value, true);
			}

			if (value instanceof List) {
				value = convertList((List<?>) value, true);
			}

			if (value instanceof ResponseEntity) {
				Field field = value.getClass().getSuperclass().getDeclaredField("body");
				ReflectionUtils.makeAccessible(field);
				Object body = field.get(value);

				afterRunMethod(body);
			}

		}

		return value;
	}

	/**
	 * 문자열 convert
	 *
	 * @param str
	 * @param isEscape
	 * @return
	 */
	private String convertString(String str, boolean isEscape) {
		return isEscape ? XssPreventer.escape(str) : XssPreventer.unescape(str);
	}

	/**
	 * 배열안의 문자열 convert
	 *
	 * @param arr
	 * @param isEscape
	 * @return
	 */
	private String[] convertStringArray(String[] arr, boolean isEscape) {
		return Stream.of(arr).map(item -> isEscape ? XssPreventer.escape(item) : XssPreventer.unescape(item)).toArray(String[]::new);
	}

	/**
	 * List객체 요소 convert
	 *
	 * @param list
	 * @param isEscape
	 * @return
	 * @throws Exception
	 */
	private List<?> convertList(List<?> list, boolean isEscape) throws Exception {
		List<Object> resuList = new ArrayList<>();

		for (int i = 0, size = list.size(); i < size; i++) {
			Object data = list.get(i);
			String pkgPath = data.getClass().getPackageName();
			String typeName = data.getClass().getSimpleName();

			if (TYPE_STRING.equalsIgnoreCase(typeName)) {
				resuList.add(convertString((String) data, isEscape));
			}

			if ((TYPE_STRING.concat(TYPE_ARRAY)).equalsIgnoreCase(typeName)) {
				resuList.add(convertStringArray((String[]) data, isEscape));
			}

			if (!(data instanceof Collection) && pkgPath.contains(PROJECT_PATH)) {
				initDtoInField(data, isEscape);
				resuList.add(data);
			}

			if (data instanceof List) {
				resuList.add(convertList((List<?>) data, isEscape));
			}
		}

		return resuList;
	}

	/**
	 * DTO 객체 필드값 convert
	 *
	 * @param fields
	 * @param dto
	 * @param isEscape
	 * @throws Exception
	 */
	private void reflectDtoField(Field[] fields, Object dto, boolean isEscape) throws Exception {
		for (int i = 0, length = fields.length; i < length; i++) {
			Field field = fields[i];
			Object value = null;

			if (!field.canAccess(dto)) {
				ReflectionUtils.makeAccessible(field);
			}

			value = field.get(dto);

			if (value != null) {
				if (value instanceof String) {
					ReflectionUtils.setField(field, dto, isEscape ? XssPreventer.escape((String) value) : XssPreventer.unescape((String) value));
				}

				if (value instanceof List) {
					ReflectionUtils.setField(field, dto, convertList((List<?>) value, isEscape));
				}
			}
		}

	}

	/**
	 * init DTO field
	 *
	 * @param dto
	 * @param isEscape
	 * @throws Exception
	 */
	private void initDtoInField(Object dto, boolean isEscape) throws Exception {
		Field[] currentFields = dto.getClass().getDeclaredFields();
		Field[] superFields = dto.getClass().getSuperclass().getDeclaredFields();

		// parent class field
		if (superFields.length > 0) {
			reflectDtoField(superFields, dto, isEscape);
		}

		// now class field
		if (currentFields.length > 0) {
			reflectDtoField(currentFields, dto, isEscape);
		}

	}
}
