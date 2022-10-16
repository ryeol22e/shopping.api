package com.project.shopping.zconfig.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.shopping.member.dto.MemberEnum;
import com.project.shopping.member.service.MemberService;
import com.project.shopping.zconfig.UtilsJwt;
import com.project.shopping.zconfig.authentications.UserAuthentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final MemberService memberService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uri = request.getRequestURI();
		boolean checkAuth = uri.equalsIgnoreCase("/api/auth/check") ? true : false;
		boolean tokenExpired = false;
		String token = "";
		Enumeration<String> headers = request.getHeaders(HttpHeaders.AUTHORIZATION);
		
		while(headers.hasMoreElements()) {
			String value = headers.nextElement();

			if(value.startsWith("Bearer ")) {
				if(!UtilsJwt.validate(token)) {
					log.info("access token is expired.");
					String memberId = request.getHeader("MemberId");
					
					token = memberService.getRefreshToken(memberId);

					if(!UtilsJwt.validate(token)) {
						memberService.removeRefreshToken(memberId);
						tokenExpired = true;
					}
				}
			}
		}

		if(checkAuth && tokenExpired) {
			throw new IOException("로그인이 만료되었습니다.");
		}

		String email = MemberEnum.ANONYMOUS.name();
		String role = MemberEnum.ANONYMOUS.getValue();

		if(!"".equals(token) && !tokenExpired) {
			email = UtilsJwt.getInfo(token).get("memberId").toString();
			role = UtilsJwt.getInfo(token).get("memberRole").toString();
		}
		
		GrantedAuthority authority = new SimpleGrantedAuthority(role);
		List<GrantedAuthority> authorities = new ArrayList<>();

		authorities.add(authority);

		UserAuthentication authentication = new UserAuthentication(email, null, authorities);

		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		log.info("{}", SecurityContextHolder.getContext().getAuthentication());
		
		filterChain.doFilter(request, response);
	}
}
