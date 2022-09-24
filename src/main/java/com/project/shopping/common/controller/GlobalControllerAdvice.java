package com.project.shopping.common.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
	
	@ModelAttribute
	public void commonLog(HttpServletRequest request) {
		String uri = request.getRequestURI();

		if(!uri.equals("/error")) {
			log.info("request uri : {}", uri);
		}
	}

	@ExceptionHandler(Exception.class)
	public void exceptionHandler(Exception e) {
		log.error(e.getMessage());
	}
}
