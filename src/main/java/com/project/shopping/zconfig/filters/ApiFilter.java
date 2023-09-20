package com.project.shopping.zconfig.filters;

import java.io.IOException;
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

public class ApiFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		MemberDTO memberInfo = (MemberDTO) request.getSession(true).getAttribute("memberInfo");
		
		if(memberInfo==null) {
			String memberName = MemberEnum.ANONYMOUS.name();
			String memberRole = MemberEnum.ANONYMOUS.getValue();
			GrantedAuthority authority = new SimpleGrantedAuthority(memberRole);
			List<GrantedAuthority> authorities = List.of(authority);
			UserAuthentication authentication = new UserAuthentication(new MemberDTO(memberName, memberRole), null, authorities);

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);
	}
}
