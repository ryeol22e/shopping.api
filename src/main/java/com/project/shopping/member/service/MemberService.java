package com.project.shopping.member.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.shopping.member.dto.MemberDTO;
import com.project.shopping.member.dto.MemberEnum;
import com.project.shopping.member.repository.MemberRepository;
import com.project.shopping.zconfig.UtilsJwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private static Map<String, String> refreshTokenMap = new LinkedHashMap<>();

	/**
	 * refresh token 가져오기
	 * @param memberId
	 * @return
	 */
	public String getRefreshToken(String memberId) {
		String token = "";
		String id = memberId.concat(".refreshToken");

		if(refreshTokenMap.containsKey(id)) {
			token = refreshTokenMap.get(id);
		} else {
			token = memberRepository.findByMemberId(memberId).getRefreshToken();
			refreshTokenMap.put(id, token);
		}

		return token;
	}

	/**
	 * 로그인
	 * @param memberId
	 * @param memberPassword
	 * @return
	 * @throws Exception
	 */
	public MemberDTO loginMember(String memberId, String memberPassword) throws Exception {
		MemberDTO member = loginProcessCheck(memberId, memberPassword);
		
		if(member==null) {
			throw new Exception("로그인에 실패했습니다.");
		}
		
		member.setAccessToken(UtilsJwt.createAccessToken(member));
		member.setRefreshToken(UtilsJwt.createRefreshToken(member));
		memberRepository.save(member);
		refreshTokenMap.put(member.getMemberId().concat(".refreshToken"), member.getRefreshToken());

		return member;
	}

	public String authNumber(MemberDTO member) throws Exception {
		log.info("data : {}", member);
		String authNumber = String.valueOf(Math.floor(Math.random()*900000));

		member.setAuthNumber(authNumber.substring(0, authNumber.lastIndexOf(".")));

		log.info("auth number is {}", authNumber);
		return authNumber;
	}

	public Boolean joinMember(MemberDTO member) throws Exception {
		Boolean result = false;
		
		member.changeBcryptPassword();

		if(MemberEnum.getAdminData().containsKey(member.getMemberId())) {
			member.setMemberRole(MemberEnum.ADMIN.getValue());
		} else {
			member.setMemberRole(MemberEnum.MEMBER.getValue());
		}

		MemberDTO memberResult = memberRepository.save(member);

		if(memberResult!=null) {
			result = true;
		}

		return result;
	}

	private MemberDTO loginProcessCheck(String memberId, String memberPassword) throws Exception {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		MemberDTO member = memberRepository.findByMemberId(memberId);
		Boolean result = true;

		if(member==null) {
			result = false;
		} else if(encoder.matches(memberPassword, member.getMemberPassword())) {
			result = false;
		}

		if(result) {
			member = null;
		}

		return member;
	}

}
