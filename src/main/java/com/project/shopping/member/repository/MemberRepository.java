package com.project.shopping.member.repository;

import static com.project.shopping.member.dto.QMemberTable.memberTable;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.project.shopping.member.dto.MemberTable;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
	@Qualifier(value = "mariadbFactory")
	private final JPAQueryFactory mariadbFactory;

	public MemberTable findByMemberId(String loginId) {
		return mariadbFactory.selectFrom(memberTable)
				.where(memberTable.memberId.eq(loginId))
				.fetchOne();
	}

	public String findRefreshByAccess(String accessToken) {
		return mariadbFactory.select(memberTable.refreshToken)
				.from(memberTable)
				.where(memberTable.accessToken.eq(accessToken))
				.fetchOne();
	}

	public long save(MemberTable dto) {
		return mariadbFactory.insert(memberTable)
				.columns(memberTable.memberId,
						memberTable.memberName,
						memberTable.memberPassword,
						memberTable.memberAddr,
						memberTable.memberEmail,
						memberTable.memberRole,
						memberTable.authNumber,
						memberTable.joinDtm)
				.values(dto.getMemberId(),
						dto.getMemberName(),
						dto.getMemberPassword(),
						dto.getMemberAddr(),
						dto.getMemberEmail(),
						dto.getMemberRole(),
						dto.getAuthNumber(),
						dto.getJoinDtm())
				.execute();
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public long updateLoginDate(MemberTable dto) {
		return mariadbFactory.update(memberTable)
				.set(memberTable.loginDtm, LocalDateTime.now())
				.set(memberTable.accessToken, dto.getAccessToken())
				.set(memberTable.refreshToken, dto.getRefreshToken())
				.where(memberTable.memberNo.eq(dto.getMemberNo()))
				.execute();
	}
}
