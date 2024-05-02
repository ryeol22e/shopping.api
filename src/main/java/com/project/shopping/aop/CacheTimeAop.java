package com.project.shopping.aop;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import com.project.shopping.zconfig.annotations.CacheTime;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author
 * @date 2024.05.01
 */
@Aspect
@Component
@RequiredArgsConstructor
public class CacheTimeAop {
	private final RedisTemplate<String, Object> redisTemplate;

	@Around("@annotation(org.springframework.cache.annotation.Cacheable) && @annotation(com.project.shopping.zconfig.annotations.CacheTime)")
	public Object cacheable(ProceedingJoinPoint joinPoint) throws Throwable {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Cacheable cacheable = method.getAnnotation(Cacheable.class);
		CacheTime cacheTime = method.getAnnotation(CacheTime.class);
		String[] values = cacheable.value();
		String key = getSPELValue(signature.getParameterNames(), joinPoint.getArgs(), cacheable.key());
		StringBuilder resultKey = new StringBuilder();
		long expired = cacheTime.time();

		for (int i = 0, length = values.length; i < length; i++) {
			resultKey.append(values[i]);

			if (i + 1 < length) {
				resultKey.append("::");
			}
		}

		if (key != null && !key.equals("")) {
			resultKey.append("::").append(key);
		}
		Object result = joinPoint.proceed();

		if (Optional.ofNullable(redisTemplate.hasKey(resultKey.toString())).isPresent()) {
			redisTemplate.opsForValue().getAndExpire(resultKey.toString(), Duration.ofHours(expired));
		}

		return result;
	}

	private String getSPELValue(String[] parameterNames, Object[] args, String name) {
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();

		for (int i = 0, length = parameterNames.length; i < length; i++) {

			context.setVariable(parameterNames[i], args[i]);

		}

		return (String) parser.parseExpression(name).getValue(context);
	}

}
