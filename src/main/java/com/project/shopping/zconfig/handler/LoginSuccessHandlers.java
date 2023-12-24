package com.project.shopping.zconfig.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopping.member.dto.MemberDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginSuccessHandlers implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		log.info("login success. > {}", authentication.getPrincipal());
		MemberDTO member = (MemberDTO) authentication.getPrincipal();

		member.clearPassword();
		// request.getSession().setMaxInactiveInterval(3600);
		// request.getSession().setAttribute("memberInfo", member);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> resMap = new HashMap<>() {
			{
				put("token", member.getAccessToken());
				put("isLogin", true);
			}
		};
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(mapper.writeValueAsString(resMap));
		
	}
}
