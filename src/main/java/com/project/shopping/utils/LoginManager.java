package com.project.shopping.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import com.project.shopping.member.dto.MemberDTO;

public class LoginManager {
	public static MemberDTO getMemberInfo() {
		return (MemberDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
