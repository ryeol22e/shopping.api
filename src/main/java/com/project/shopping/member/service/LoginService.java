package com.project.shopping.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.project.shopping.common.dto.TokenInfo;
import com.project.shopping.common.repository.TokenRepository;
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
    private final TokenRepository tokenRepository;

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

            Cookie tokenCookie = new Cookie("LOGIN_ID", memberId);

            tokenCookie.setPath("/");
            tokenCookie.setHttpOnly(true);
            tokenCookie.setSecure(true);
            response.addCookie(tokenCookie);
            TokenInfo tokenInfo = new TokenInfo(memberId, member.getAccessToken(), member.getRefreshToken());

            tokenRepository.save(tokenInfo);
            result = true;
        } catch (NullPointerException e) {
            log.error("login error : {}", e);
            throw new NullPointerException("로그인에 실패했습니다.");
        }

        return result;
    }

    /**
     * refresh token 삭제
     * 
     * @param memberId
     */
    public void removeRefreshToken(String memberId) {
        tokenRepository.deleteById(memberId);
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
        boolean result = false;

        if (member != null && encoder.matches(memberPassword, member.getMemberPassword())) {
            result = true;
        }

        return result ? member : null;
    }

}
