package com.project.shopping.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
	private static final String TYPE_STRING = "String";
	private static final String TYPE_STRING_ARRAY = TYPE_STRING.concat("[]");

	@Around("execution(* com.project.shopping.*.repository.*.*(..))")
	public Object executeXssFilter(ProceedingJoinPoint joinPoint) throws Throwable {
		// @Before : escape 처리
		Object[] newArgs = beforeRunMethod(joinPoint.getArgs());
		// s : method 실행
		Object returnValue = joinPoint.proceed(newArgs);

		// @After || @AfterReturning : unescape 처리
		return afterRunMethod(returnValue);
	}

	/**
	 * 전처리
	 * 
	 * @param orgArgs
	 * @return
	 * @throws Throwable
	 */
	private Object[] beforeRunMethod(Object[] orgArgs) throws Throwable {
		List<Object> newArgs = new ArrayList<>();

		for (int i = 0, length = orgArgs.length; i < length; i++) {
			Object arg = orgArgs[i];
			String pkgPath = arg.getClass().getPackageName();
			String typeName = arg.getClass().getSimpleName();

			if (TYPE_STRING.equalsIgnoreCase(typeName) || TYPE_STRING_ARRAY.equalsIgnoreCase(typeName)) {
				newArgs.add(typeName.contains("[]") ? convertStringArray((String[]) arg, true) : convertString((String) arg, true));
			}

			if (arg instanceof List) {
				List<?> list = (List<?>) arg;
				newArgs.add(isListTypeString(list) ? stringTypeList(list, true) : dtoToList(list, true));
			}

			if (pkgPath.contains("com.project.shopping")) {
				dtoInField(arg, true);
				newArgs.add(arg);
			}

		}


		return newArgs.toArray();
	}

	/**
	 * 후처리
	 * 
	 * @param value
	 * @return
	 * @throws Throwable
	 */
	private Object afterRunMethod(Object value) throws Throwable {
		if (value != null) {
			String pkgPath = value.getClass().getPackageName();
			String typeName = value.getClass().getSimpleName();

			if (TYPE_STRING.equalsIgnoreCase(typeName) || TYPE_STRING_ARRAY.equalsIgnoreCase(typeName)) {
				value = typeName.contains("[]") ? convertStringArray((String[]) value, false) : convertString((String) value, false);
			}

			if (value instanceof List) {
				List<?> list = (List<?>) value;
				value = isListTypeString(list) ? stringTypeList(list, false) : dtoToList(list, false);
			}

			if (pkgPath.contains("com.project.shopping")) {
				dtoInField(value, false);
			}
		}

		return value;
	}

	private String convertString(String str, boolean isEscape) {
		return isEscape ? XssPreventer.escape(str) : XssPreventer.unescape(str);
	}

	private String[] convertStringArray(String[] arr, boolean isEscape) {
		return Stream.of(arr).map(item -> isEscape ? XssPreventer.escape(item) : XssPreventer.unescape(item)).toArray(String[]::new);
	}

	private List<String> stringTypeList(List<?> list, boolean isEscape) {
		List<String> resultList = new ArrayList<>();

		for (int i = 0, size = list.size(); i < size; i++) {
			String data = (String) list.get(i);

			if (isEscape) {
				resultList.add(XssPreventer.escape(data));
			} else {
				resultList.add(XssPreventer.unescape(data));
			}
		}

		return resultList;
	}

	private List<?> dtoToList(List<?> list, boolean isEscape) throws Throwable {
		List<Object> result = new ArrayList<>();

		for (int i = 0, size = list.size(); i < size; i++) {
			Object data = list.get(i);

			dtoInField(data, isEscape);
			result.add(data);
		}

		return result;
	}

	private boolean isListTypeString(List<?> list) throws NoSuchMethodException {
		Method method = getClass().getDeclaredMethod("isListTypeString", List.class);
		ReflectionUtils.makeAccessible(method);
		Type type = ((ParameterizedType) method.getGenericParameterTypes()[0]).getActualTypeArguments()[0];

		String typeName = "";
		if (list != null && !list.isEmpty()) {
			typeName = list.get(0).getClass().getSimpleName();
		}

		return TYPE_STRING.equalsIgnoreCase(typeName);
	}

	private void listInField(Object dto, Field field, boolean isEscape) throws Throwable {
		List<?> list = (List<?>) field.get(dto);

		if (Optional.ofNullable(list).isPresent()) {
			if (isListTypeString(list)) {
				List<Object> result = new ArrayList<>();

				for (int i = 0, size = list.size(); i < size; i++) {
					result.add(convertString((String) list.get(i), isEscape));
				}

				ReflectionUtils.setField(field, dto, result);
			} else {
				for (int i = 0, size = list.size(); i < size; i++) {
					Object obj = list.get(i);
					dtoInField(obj, isEscape);
				}
			}
		}

	}

	private void reflectDtoField(Field[] fields, Object dto, boolean isEscape) throws Throwable {
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
					listInField(dto, field, isEscape);
				}
			}

		}

	}

	private void dtoInField(Object dto, boolean isEscape) throws Throwable {
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
