package com.project.shopping.zconfig;

import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.shopping.member.dto.MemberDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class UtilsJwt {
	private static String AUTH_KEY;
	private static final String PRE_AUTH = "Bearer ";

	@Value("${jwt.auth.key}")
	private void setKey(String value) {
		AUTH_KEY = value;
	}
	/**
	 * access 생성
	 * @param email
	 * @return
	 */
	public static String createAccessToken(MemberDTO member) {
		Date now = new Date();
		
		return Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setIssuer("fresh")
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + Duration.ofMinutes(30L).toMillis()))
			.claim("memberId", member.getMemberId())
			.claim("memberEmail", member.getMemberEmail())
			.claim("memberName", member.getMemberName())
			.claim("memberRole", member.getMemberRole())
			.signWith(SignatureAlgorithm.HS256, AUTH_KEY)
			.compact();
	}

	/**
	 * refreshToken 생성
	 * @param member
	 * @return
	 */
	public static String createRefreshToken(MemberDTO member) {
		Date now = new Date();

		return Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setIssuer("fresh")
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + Duration.ofDays(2L).toMillis()))
			.claim("memberId", member.getMemberId())
			.claim("memberEmail", member.getMemberEmail())
			.claim("memberName", member.getMemberName())
			.claim("memberRole", member.getMemberRole())
			.signWith(SignatureAlgorithm.HS256, AUTH_KEY)
			.compact();
	}

	/**
	 * 토큰정보 취득
	 * @param token
	 * @return
	 */
	public static Claims getInfo(String token) {
		return Jwts.parser().setSigningKey(AUTH_KEY).parseClaimsJws(combineToken(token)).getBody();
	}

	/**
	 * 토큰 validation
	 * @param token
	 * @return
	 */
	public static boolean validate(String token)  {
		boolean flag = true;

		try {
			Jwts.parser().setSigningKey(AUTH_KEY).parseClaimsJws(combineToken(token));
		} catch (Exception e) {
			//TODO: handle exception
			flag = false;
		}

		return flag;
	}

	private final static String combineToken(String token) {
		if(token.startsWith(PRE_AUTH)) {
			token = token.substring(PRE_AUTH.length());
		}

		return token;
	}
	
}
