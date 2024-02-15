package com.project.shopping.member.repository;

import static com.project.shopping.member.dto.QMemberDTO.memberDTO;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.project.shopping.member.dto.MemberDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
	@Qualifier(value = "mariadbFactory")
	private final JPAQueryFactory mariadbFactory;

	public MemberDTO findByMemberId(String loginId) {
		return mariadbFactory.selectFrom(memberDTO)
				.where(memberDTO.memberId.eq(loginId))
				.fetchOne();
	}

	public String findRefreshByAccess(String accessToken) {
		return mariadbFactory.select(memberDTO.refreshToken)
				.from(memberDTO)
				.where(memberDTO.accessToken.eq(accessToken))
				.fetchOne();
	}

	public long save(MemberDTO dto) {
		return mariadbFactory.insert(memberDTO)
				.columns(memberDTO).values(dto)
				.execute();
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public long updateLoginDate(MemberDTO dto) {
		return mariadbFactory.update(memberDTO)
				.set(memberDTO.loginDtm, LocalDateTime.now())
				.set(memberDTO.accessToken, dto.getAccessToken())
				.set(memberDTO.refreshToken, dto.getRefreshToken())
				.where(memberDTO.memberNo.eq(dto.getMemberNo()))
				.execute();
	}
}
