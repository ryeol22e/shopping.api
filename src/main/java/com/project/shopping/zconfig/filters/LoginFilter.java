package com.project.shopping.zconfig.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.shopping.member.dto.MemberDTO;
import com.project.shopping.member.dto.MemberEnum;
import com.project.shopping.zconfig.authentications.UserAuthentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String memberId = MemberEnum.ANONYMOUS.name();
		String role = MemberEnum.ANONYMOUS.getValue();
		MemberDTO memberInfo = (MemberDTO) request.getSession(true).getAttribute("memberInfo");
		
		if(memberInfo!=null) {
			memberId = memberInfo.getMemberId();
			role = memberInfo.getMemberRole();
		}

		GrantedAuthority authority = new SimpleGrantedAuthority(role);
		List<GrantedAuthority> authorities = new ArrayList<>();

		authorities.add(authority);

		UserAuthentication authentication = new UserAuthentication(memberId, null, authorities);

		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		log.info("{}", SecurityContextHolder.getContext().getAuthentication());
		
		filterChain.doFilter(request, response);
	}
}
