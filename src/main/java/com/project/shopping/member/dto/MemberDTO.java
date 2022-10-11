package com.project.shopping.member.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.shopping.member.dto.pk.MemberPK;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@IdClass(MemberPK.class)
@Table(name = "MEMBER_TABLE")
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(value = {"changeBcryptPassword", "memberPassword", "updateLoginDtm", "setAuthNumber"}, allowSetters = true)
public class MemberDTO {
	@Id
	@Column(name = "MEMBER_NO")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long memberNo;
	@Id
	@Column(name = "MEMBER_ID")
	private String memberId;
	private String memberPassword;
	private String memberEmail;
	private String memberName;
	private String memberAddr;
	private String memberToken;
	private String refreshToken;
	private String tempYn;
	private String authNumber;
	private String memberRole;
	@CreationTimestamp
	private LocalDateTime joinDtm = LocalDateTime.now();
	@UpdateTimestamp
	private LocalDateTime loginDtm;

	public void setAuthNumber(String authNum) {
	  this.authNumber = authNum;
	}

	public void setMemberRole(String memberRole) {
		this.memberRole = memberRole;
	}
	
	public void changeBcryptPassword() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		this.memberPassword = encoder.encode(getMemberPassword());
	}

	public void updateLoginDtm() {
		this.loginDtm = LocalDateTime.now();
	}
	
}
