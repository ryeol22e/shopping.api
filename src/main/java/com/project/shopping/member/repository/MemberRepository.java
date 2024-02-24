package com.project.shopping.member.repository;

import static com.project.shopping.member.dto.QMemberInfo.memberInfo;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.project.shopping.member.dto.MemberInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
	@Qualifier(value = "mariadbFactory")
	private final JPAQueryFactory mariadbFactory;

	public MemberInfo findByMemberId(String loginId) {
		return mariadbFactory.selectFrom(memberInfo)
				.where(memberInfo.memberId.eq(loginId))
				.fetchOne();
	}

	public String findRefreshByAccess(String accessToken) {
		return mariadbFactory.select(memberInfo.refreshToken)
				.from(memberInfo)
				.where(memberInfo.accessToken.eq(accessToken))
				.fetchOne();
	}

	public long save(MemberInfo dto) {
		return mariadbFactory.insert(memberInfo)
				.columns(memberInfo.memberId,
						memberInfo.memberName,
						memberInfo.memberPassword,
						memberInfo.memberAddr,
						memberInfo.memberEmail,
						memberInfo.memberRole,
						memberInfo.authNumber,
						memberInfo.joinDtm)
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
	public long updateLoginDate(MemberInfo dto) {
		return mariadbFactory.update(memberInfo)
				.set(memberInfo.loginDtm, LocalDateTime.now())
				.set(memberInfo.accessToken, dto.getAccessToken())
				.set(memberInfo.refreshToken, dto.getRefreshToken())
				.where(memberInfo.memberNo.eq(dto.getMemberNo()))
				.execute();
	}
}
