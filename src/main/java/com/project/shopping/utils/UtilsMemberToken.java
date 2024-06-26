package com.project.shopping.utils;

import java.time.Duration;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.project.shopping.member.dto.MemberInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class UtilsMemberToken {
    private static String authKey;
    private static final String PRE_AUTH = "Bearer ";

    @Value("${jwt.auth.key}")
    private synchronized void setKey(String value) {
        authKey = value;
    }

    /**
     * access 생성
     * 
     * @param email
     * @return
     */
    public static String createAccessToken(MemberInfo member) {
        Date now = new Date();

        return Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE).setIssuer("fresh")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30L).toMillis()))
                .claim("memberNo", member.getMemberNo()).claim("memberName", member.getMemberName())
                .claim("memberRole", member.getMemberRole())
                .signWith(SignatureAlgorithm.HS256, authKey).compact();
    }

    /**
     * refreshToken 생성
     * 
     * @param member
     * @return
     */
    public static String createRefreshToken(MemberInfo member) {
        Date now = new Date();

        return Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE).setIssuer("fresh")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofDays(30L).toMillis()))
                .claim("memberNo", member.getMemberNo()).claim("memberName", member.getMemberName())
                .claim("memberRole", member.getMemberRole())
                .signWith(SignatureAlgorithm.HS256, authKey).compact();
    }

    /**
     * 토큰정보 취득
     * 
     * @param token
     * @return
     */
    public static Claims getInfo(String token) {
        Claims claims = null;

        try {
            claims = Jwts.parser().setSigningKey(authKey).parseClaimsJws(combineToken(token))
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }

        return claims;
    }

    /**
     * 토큰 validation
     * 
     * @param token
     * @return
     */
    public static boolean validate(String token) {
        boolean flag = true;

        try {
            Jwts.parser().setSigningKey(authKey).parseClaimsJws(combineToken(token));
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    private static String combineToken(String token) {
        if (token.startsWith(PRE_AUTH)) {
            token = token.substring(PRE_AUTH.length());
        }

        return token;
    }

}
