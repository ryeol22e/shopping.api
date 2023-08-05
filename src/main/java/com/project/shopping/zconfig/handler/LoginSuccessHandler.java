package com.project.shopping.zconfig.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		log.info("login success. > {}", authentication.getPrincipal());

		request.getSession().setMaxInactiveInterval(3600);
		request.getSession().setAttribute("memberInfo", authentication.getPrincipal());
		response.getWriter().write(new ObjectMapper().writeValueAsString(true));
		
	}
}
