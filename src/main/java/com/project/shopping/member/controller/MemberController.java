package com.project.shopping.member.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.shopping.member.dto.MemberDTO;
import com.project.shopping.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
	private final MemberService memberService;

	@GetMapping("/info")
	public ResponseEntity<Map<String, Object>> defaultInfo(HttpServletRequest request) {
		MemberDTO info = (MemberDTO) request.getSession().getAttribute("memberInfo");
		Map<String, Object> defaultInfo = new HashMap<>();

		if(info!=null) {
			defaultInfo.put("memberNo", info.getMemberNo());
			defaultInfo.put("memberName", info.getMemberName());
			defaultInfo.put("memberRole", info.getMemberRole());
		}
		return ResponseEntity.ok(defaultInfo);
	}

	@PostMapping("/join")
	public ResponseEntity<Boolean> join(@RequestBody MemberDTO member) {
		return ResponseEntity.ok(memberService.joinMember(member));
	}

	@PostMapping("/auth/number")
	public ResponseEntity<String> authNumber(@RequestBody MemberDTO member) {
		return ResponseEntity.ok(memberService.authNumber(member));
	}
}
