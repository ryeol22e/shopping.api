package com.project.shopping.zconfig.filters;

import java.io.IOException;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.project.shopping.member.dto.MemberEnum;
import com.project.shopping.member.dto.MemberInfo;
import com.project.shopping.member.service.MemberService;
import com.project.shopping.zconfig.authentications.UserAuthentication;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ApiFilter extends OncePerRequestFilter {
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String memberNo = "99999";
        String memberName = MemberEnum.ANONYMOUS.name();
        String memberRole = MemberEnum.ANONYMOUS.getValue();
        Claims info = memberService.getTokenInfo(request);

        if (info != null) {
            memberNo = info.get("memberNo").toString();
            memberName = info.get("memberName").toString();
            memberRole = info.get("memberRole").toString();
        } else {
            Cookie cookie = new Cookie("LOGIN_ID", null);

            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }


        GrantedAuthority authority = new SimpleGrantedAuthority(memberRole);
        List<GrantedAuthority> authorities = List.of(authority);
        UserAuthentication authentication = new UserAuthentication(new MemberInfo(Long.parseLong(memberNo), memberName, memberRole), null, authorities);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
