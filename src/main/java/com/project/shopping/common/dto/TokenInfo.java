package com.project.shopping.common.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@RedisHash(value = "token", timeToLive = 3600L)
public class TokenInfo {
    @Id
    private final String memberId;
    private final String accessToken;
    private final String refreshToken;

}
