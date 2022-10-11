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

import com.project.shopping.zconfig.UtilsJwt;
import com.project.shopping.zconfig.authentications.UserAuthentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Enumeration<String> headers = request.getHeaders(HttpHeaders.AUTHORIZATION);
		
		while(headers.hasMoreElements()) {
			String value = headers.nextElement();

			if(value.toLowerCase().startsWith("bearer")) {
				String token = value.substring("Bearer ".length());

				if(!UtilsJwt.validate(token)) {
					// Cookie cookie = new Cookie();
					
					// cookie.setMaxAge(0);
					// response.addCookie(cookie);
					throw new IOException("로그인이 만료되었습니다.");
				} else {
					String email = UtilsJwt.getInfo(token).get("memberEmail").toString();
					String role = UtilsJwt.getInfo(token).get("memberRole").toString();
					GrantedAuthority authority = new SimpleGrantedAuthority(role);
					List<GrantedAuthority> authorities = new ArrayList<>();

					authorities.add(authority);

					UserAuthentication authentication = new UserAuthentication(email, null, authorities);

					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
					SecurityContextHolder.getContext().setAuthentication(authentication);

					log.info("{}", SecurityContextHolder.getContext().getAuthentication());
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}
}
