package com.project.shopping.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import com.project.shopping.member.dto.MemberTable;

public class LoginManager {
	public static MemberTable getMemberInfo() {
		return (MemberTable) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
