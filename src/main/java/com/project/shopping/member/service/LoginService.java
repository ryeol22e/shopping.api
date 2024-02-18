package com.project.shopping.member.service;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.project.shopping.member.dto.MemberDTO;
import com.project.shopping.member.repository.MemberRepository;
import com.project.shopping.utils.UtilsMemberToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
	private final MemberRepository memberRepository;
	private static Map<String, String> refreshTokenMap = new LinkedHashMap<>();

	/**
	 * 로그인
	 */
	@Override
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		MemberDTO member = memberRepository.findByMemberId(memberId);
		// HttpServletRequest request = ((ServletRequestAttributes)
		// RequestContextHolder.currentRequestAttributes()).getRequest();

		try {
			member.setAccessToken(UtilsMemberToken.createAccessToken(member));
			member.setRefreshToken(UtilsMemberToken.createRefreshToken(member));
			memberRepository.updateLoginDate(member);

			// request.getSession().setMaxInactiveInterval(3600);
			// request.getSession().setAttribute("memberInfo", member);
		} catch (NullPointerException e) {
			log.error("login error : {}", e.getMessage());
			throw new NullPointerException("로그인에 실패했습니다.");
		}

		return member;
	}

	/**
	 * 로그인
	 * 
	 * @param memberId
	 * @param memberPassword
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public boolean loginMember(MemberDTO reqMember) {
		boolean result = false;
		String memberId = reqMember.getMemberId();
		String memberPassword = reqMember.getMemberPassword();
		MemberDTO member = loginProcessCheck(memberId, memberPassword);
		HttpServletRequest request =
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
						.getRequest();

		try {
			member.setAccessToken(UtilsMemberToken.createAccessToken(member));
			member.setRefreshToken(UtilsMemberToken.createRefreshToken(member));
			memberRepository.save(member);

			request.getSession().setMaxInactiveInterval(3600);
			request.getSession().setAttribute("memberInfo", member);
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
	@Deprecated
	private MemberDTO loginProcessCheck(String memberId, String memberPassword) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		MemberDTO member = memberRepository.findByMemberId(memberId);
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
