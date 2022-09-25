package com.project.shopping.member.dto.pk;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberPK implements Serializable {
	private Long memberNo;
	private String memberId;
}
