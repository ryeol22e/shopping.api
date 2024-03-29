package com.project.shopping.member.service;

import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.shopping.member.dto.MemberInfo;
import com.project.shopping.member.dto.MemberEnum;
import com.project.shopping.member.repository.MemberRepository;
import com.project.shopping.utils.UtilsMemberToken;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	public String authNumber(MemberInfo member) {
		log.info("data : {}", member);
		String authNumber = String.valueOf(Math.floor(Math.random() * 900000));

		member.setAuthNumber(authNumber.substring(0, authNumber.lastIndexOf(".")));

		log.info("auth number is {}", authNumber);
		return authNumber;
	}

	@Transactional
	public Boolean joinMember(MemberInfo member) {
		Boolean result = false;

		member.changeBcryptPassword();

		if (MemberEnum.getAdminData().containsKey(member.getMemberId())) {
			member.setMemberRole(MemberEnum.ADMIN.getValue());
		} else {
			member.setMemberRole(MemberEnum.MEMBER.getValue());
		}

		long memberResult = memberRepository.save(member);

		if (memberResult != 0) {
			result = true;
		}

		return result;
	}

	public String getRefreshByAccessToken(String accessToken) {
		return memberRepository.findRefreshByAccess(accessToken);
	}

	public String getToken(HttpServletRequest request) {
		String resultToken = "";
		Cookie[] cookies = request.getCookies();
		String accessToken =
				cookies != null && cookies.length > 0
						? Stream.of(cookies)
								.filter(cookie -> "ACCESS_TOKEN".equalsIgnoreCase(cookie.getName()))
								.map(cookie -> cookie.getValue()).findFirst().orElse("")
						: "";

		if (accessToken != null && (!accessToken.isEmpty() && !accessToken.isBlank())) {
			try {
				Claims jwt = UtilsMemberToken.getInfo(accessToken);

				if (jwt != null) {
					resultToken = accessToken;
				} else {
					String refreshToken = getRefreshByAccessToken(accessToken);
					jwt = UtilsMemberToken.getInfo(refreshToken);

					if (jwt != null) {
						resultToken = refreshToken;
					}
				}
			} catch (Exception e) {
				String refreshToken = getRefreshByAccessToken(accessToken);
				Claims jwt = UtilsMemberToken.getInfo(refreshToken);

				if (jwt != null) {
					resultToken = refreshToken;
				}
			}
		}

		return resultToken;
	}

	public boolean checkToken(HttpServletRequest request) {
		String token = getToken(request);

		return !token.isEmpty() && !token.isBlank() ? true : false;
	}

}
