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
import com.project.shopping.utils.UtilsMemberToken;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
	private final MemberService memberService;

	@GetMapping("/info")
	public ResponseEntity<Map<String, Object>> defaultInfo(HttpServletRequest request) {
		// MemberDTO info = (MemberDTO) request.getSession().getAttribute("memberInfo");
		Map<String, Object> defaultInfo = new HashMap<>();

		if (memberService.checkToken(request)) {
			Claims info = UtilsMemberToken.getInfo(memberService.getToken(request));
			String memberNo = info.get("memberNo").toString();
			String memberName = info.get("memberName").toString();
			String memberRole = info.get("memberRole").toString();

			defaultInfo.put("memberNo", memberNo);
			defaultInfo.put("memberName", memberName);
			defaultInfo.put("memberRole", memberRole);
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
