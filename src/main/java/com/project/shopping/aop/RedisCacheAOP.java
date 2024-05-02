package com.project.shopping.aop;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.project.shopping.zconfig.annotations.RedisCacheable;

@Aspect
@Component
public class RedisCacheAOP {
	private RedisTemplate<String, Object> redisTemplate;

	@Around("@annotation(com.project.shopping.zconfig.annotations.RedisCacheable)")
	public Object redisCacheable(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		RedisCacheable redisCacheable = method.getAnnotation(RedisCacheable.class);
		String cacheKey = generateKey(redisCacheable.key(), joinPoint, false);
		long expired = -1L;

		if (Optional.ofNullable(redisTemplate.hasKey(cacheKey)).isPresent()) {
			return redisTemplate.opsForValue().get(cacheKey);
		}

		Object returnValue = joinPoint.proceed();
		if (expired < 0) {
			redisTemplate.opsForValue().set(cacheKey, returnValue);
		} else {
			redisTemplate.opsForValue().set(cacheKey, returnValue, expired, TimeUnit.HOURS);
		}

		return returnValue;
	}

	private String generateKey(String key, ProceedingJoinPoint joinPoint, boolean hasTargetName) {
		String generateKey = StringUtils.arrayToCommaDelimitedString(joinPoint.getArgs());

		if (hasTargetName) {
			String target = joinPoint.getTarget().getClass().getSimpleName();
			String method = joinPoint.getSignature().getName();
			return key.concat("::").concat(target).concat(method);
		}
		return key.concat("::").concat(generateKey);
	}

}
