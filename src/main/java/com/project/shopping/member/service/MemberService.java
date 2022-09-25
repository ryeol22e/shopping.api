package com.project.shopping.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.shopping.member.dto.MemberDTO;
import com.project.shopping.member.repository.MemberRepository;
import com.project.shopping.zconfig.UtilsJwt;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	public String loginMember(String memberId, String memberPassword) throws Exception {
		MemberDTO member = loginCheck(memberId, memberPassword);

		if(member==null) {
			throw new Exception("로그인에 실패했습니다.");
		}
		
		member.updateLoginDtm();
		memberRepository.save(member);

		return UtilsJwt.createToken(member);
	}

	public String authNumber(MemberDTO member) throws Exception {
		String authNumber = String.valueOf(Math.floor(Math.random()*900000));

		member.setAuthNumber(authNumber.substring(0, authNumber.lastIndexOf(".")));
		memberRepository.save(member);

		return authNumber;
	}

	public Boolean joinMember(MemberDTO member) throws Exception {
		Boolean result = false;

		if(joinCheckAuthNum(member.getAuthNumber(), member)) {
			member.changeBcryptPassword();
			MemberDTO memberResult = memberRepository.save(member);

			if(memberResult!=null) {
				result = true;
			}
		}

		return result;
	}

	private Boolean joinCheckAuthNum(String authNumber, MemberDTO member) throws Exception {
		MemberDTO getMember = memberRepository.findByMemberId(member.getMemberId());
		Boolean result = false;

		if(authNumber!=null) {
			if(authNumber==getMember.getAuthNumber()) {
				result = true;
			}
		}

		return result;
	}

	private MemberDTO loginCheck(String memberId, String memberPassword) throws Exception {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		MemberDTO member = memberRepository.findByMemberId(memberId);
		Boolean result = true;

		if(member==null) {
			result = false;
		}
		if(encoder.matches(memberPassword, member.getMemberPassword())) {
			result = false;
		}

		if(result) {
			member = null;
		}

		return member;
	}

}
