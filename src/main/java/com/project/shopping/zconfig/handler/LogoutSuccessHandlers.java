package com.project.shopping.zconfig.handler;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LogoutSuccessHandlers implements LogoutSuccessHandler {
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		Cookie cookie = new Cookie("ACCESS_TOKEN", null);

		cookie.setMaxAge(0);
		cookie.setPath("/");
		
		response.addCookie(cookie);
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(String.valueOf(true));
	}
}
