package com.project.shopping.aop;

import java.util.stream.Stream;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.project.shopping.product.dto.ProductDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class GloabalAOP {
	// @annotation(org.springframework.web.bind.annotation.RestController)
	@Before("execution(* com.project.shopping.*.controller.*Controller.*(..))")
	public void insertRequestURI() {
		HttpServletRequest request =
				((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
						.getRequest();
		String uri = request.getRequestURI();

		log.info("request uri : {}", uri);
	}

	@Before("execution(* com.project.shopping.display.controller.*Controller.*(..))")
	public void setPageable(JoinPoint joinPoint) {
		ProductDTO product = (ProductDTO) Stream.of(joinPoint.getArgs())
				.filter(ProductDTO.class::isInstance).findFirst().orElse(new ProductDTO());

		log.info("product dto : {}", product);
		log.info("joinpoint signature : {}", joinPoint.getSignature());
	}

	@Around("(!execution(* com.project.shopping.member.service.MemberService.checkToken(..))"
			+ "&& !execution(* com.project.shopping.member.service.MemberService.getToken(..))"
			+ "&& execution(* com.project.shopping.*.service.*Service.*(..)))")
	public Object bussinessProcessTime(ProceedingJoinPoint joinPoint) {
		Object res = null;
		long start = System.nanoTime();

		try {
			res = joinPoint.proceed();
		} catch (Throwable th) {
			log.error("bussiness process error : {}", th.getMessage());
		} finally {
			long end = System.nanoTime();
			log.info("bussiness service name : \"{}\", process time : {}",
					joinPoint.getSignature().getName(), ((double) end - start) / 1000000000);
		}

		return res;
	}
}
