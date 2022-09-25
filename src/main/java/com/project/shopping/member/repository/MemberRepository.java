package com.project.shopping.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.shopping.member.dto.MemberDTO;

@Repository
public interface MemberRepository extends JpaRepository<MemberDTO, Long> {
	public MemberDTO findByMemberId(String loginId);
}
