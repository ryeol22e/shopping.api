package com.project.shopping.member.service;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.project.shopping.member.dto.MemberInfo;
import com.project.shopping.member.repository.MemberRepository;
import com.project.shopping.utils.UtilsMemberToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
	private final MemberRepository memberRepository;
	// redis 적용전 임시
	private static Map<String, String> refreshTokenMap = new LinkedHashMap<>();

	/**
	 * 로그인 프로세스
	 * 
	 * @param memberInfo
	 * @return
	 */
	public boolean loginMemberProcess(MemberInfo memberInfo) {
		boolean result = false;
		String memberId = memberInfo.getMemberId();
		String memberPassword = memberInfo.getMemberPassword();
		MemberInfo member = loginProcessCheck(memberId, memberPassword);
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

		try {
			member.setAccessToken(UtilsMemberToken.createAccessToken(member));
			member.setRefreshToken(UtilsMemberToken.createRefreshToken(member));
			memberRepository.updateLoginDate(member);

			Cookie tokenCookie = new Cookie("ACCESS_TOKEN", member.getAccessToken());

			tokenCookie.setPath("/");
			tokenCookie.setHttpOnly(true);
			tokenCookie.setSecure(true);
			response.addCookie(tokenCookie);
			result = true;
		} catch (NullPointerException e) {
			log.error("login error : {}", e.getMessage());
			throw new NullPointerException("로그인에 실패했습니다.");
		}

		return result;
	}

	/**
	 * refresh token 가져오기
	 * 
	 * @param memberId
	 * @return
	 */
	public String getRefreshToken(String memberId) {
		String token = "";
		String id = memberId.concat(".refreshToken");

		if (refreshTokenMap.containsKey(id)) {
			token = refreshTokenMap.get(id);
		} else {
			token = memberRepository.findByMemberId(memberId).getRefreshToken();
			refreshTokenMap.put(id, token);
		}

		return token;
	}

	/**
	 * refresh token 삭제
	 * 
	 * @param memberId
	 */
	public void removeRefreshToken(String memberId) {
		if (refreshTokenMap.containsKey(memberId)) {
			refreshTokenMap.remove(memberId);
		}
	}

	/**
	 * 로그인 프로세스
	 * 
	 * @param memberId
	 * @param memberPassword
	 * @return
	 */
	private MemberInfo loginProcessCheck(String memberId, String memberPassword) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		MemberInfo member = memberRepository.findByMemberId(memberId);
		Boolean result = true;

		if (member == null) {
			result = false;
		} else if (encoder.matches(memberPassword, member.getMemberPassword())) {
			result = false;
		}

		if (result) {
			member = null;
		}

		return member;
	}

}
