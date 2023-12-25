package com.project.shopping.member.repository;

import static com.project.shopping.member.dto.QMemberDTO.memberDTO;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import com.project.shopping.member.dto.MemberDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
	private final JPAQueryFactory factory;

	public MemberDTO findByMemberId(String loginId) {
		return factory.selectFrom(memberDTO)
				.where(memberDTO.memberId.eq(loginId))
				.fetchOne();
	}

	public String findRefreshByAccess(String accessToken) {
		return factory.select(memberDTO.refreshToken)
			.from(memberDTO)
			.where(memberDTO.accessToken.eq(accessToken))
			.fetchOne();
	}

	public long save(MemberDTO dto) {
		return factory.insert(memberDTO)
				.columns(memberDTO).values(dto)
				.execute();
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public long updateLoginDate(MemberDTO dto) {
		return factory.update(memberDTO)
				.set(memberDTO.loginDtm, LocalDateTime.now())
				.set(memberDTO.accessToken, dto.getAccessToken())
				.set(memberDTO.refreshToken, dto.getRefreshToken())
				.where(memberDTO.memberNo.eq(dto.getMemberNo()))
				.execute();
	}
}
