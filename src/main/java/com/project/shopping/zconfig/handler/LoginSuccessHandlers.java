package com.project.shopping.zconfig.handler;

import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import com.project.shopping.member.dto.MemberInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Deprecated
public class LoginSuccessHandlers implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		log.info("login success. > {}", authentication.getPrincipal());
		MemberInfo member = (MemberInfo) authentication.getPrincipal();

		member.clearPassword();
		// request.getSession().setMaxInactiveInterval(3600);
		// request.getSession().setAttribute("memberInfo", member);

		Cookie tokenCookie = new Cookie("LOGIN_ID", member.getMemberId());

		tokenCookie.setHttpOnly(true);
		tokenCookie.setPath("/");

		response.addCookie(tokenCookie);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write("true");

	}
}
