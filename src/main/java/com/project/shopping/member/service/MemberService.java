package com.project.shopping.member.service;

import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.shopping.common.dto.TokenInfo;
import com.project.shopping.common.repository.TokenRepository;
import com.project.shopping.member.dto.MemberEnum;
import com.project.shopping.member.dto.MemberInfo;
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
    private final TokenRepository tokenRepository;

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

    public String getToken(String memberId) {
        TokenInfo info = tokenRepository.findById(memberId).orElse(null);
        String resultToken = "";

        try {
            String accessToken = info.getAccessToken();
            String refreshToken = info.getRefreshToken();
            Claims jwt = UtilsMemberToken.getInfo(accessToken);

            if (jwt != null) {
                resultToken = info.getAccessToken();
            } else {
                jwt = UtilsMemberToken.getInfo(refreshToken);

                if (jwt != null) {
                    resultToken = refreshToken;
                }
            }
        } catch (Exception e) {
            log.error("not exists token", e.getMessage());
        }

        return resultToken;
    }

    public Claims getTokenInfo(HttpServletRequest request) {
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
        String memberId = Stream.of(cookies)
                .filter(cookie -> "LOGIN_ID".equalsIgnoreCase(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst().orElse("");

        return UtilsMemberToken.getInfo(this.getToken(memberId));
    }

}
