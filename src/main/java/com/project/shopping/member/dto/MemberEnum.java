package com.project.shopping.member.dto;

import java.util.HashMap;
import java.util.Map;

public enum MemberEnum {
	ADMIN("10003"),
	MEMBER("10000"),
	ANONYMOUS("99999");

	private String roleType;

	MemberEnum(String roleType) {
		this.roleType = roleType;
	}

	public static Map<String, Object> getAdminData() {
		Map<String, Object> admin = new HashMap<>();

		admin.put("ryeol22e", ADMIN.roleType);

		return admin;
	}

	public String getValue() {
		return this.roleType;
	}


}
