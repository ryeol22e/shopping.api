package com.project.shopping.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import com.project.shopping.member.dto.MemberInfo;

public class LoginManager {
	public static MemberInfo getMemberInfo() {
		return (MemberInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
