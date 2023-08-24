package com.project.shopping.member.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@IdClass(MemberPK.class)
@Table(name = "MEMBER_TABLE")
@ToString
@NoArgsConstructor
// setters response 보낼때 제외시킴, getters request 받을때 제외
@JsonIgnoreProperties(value = { "changeBcryptPassword", "memberPassword", "updateLoginDtm", "setAuthNumber",
		"refreshToken" }, allowSetters = true)
public class MemberDTO implements UserDetails {
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
	@Transient
	private String accessToken;
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

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void changeBcryptPassword() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		this.memberPassword = encoder.encode(getMemberPassword());
	}

	public void updateLoginDtm() {
		this.loginDtm = LocalDateTime.now();
	}

	@Override
	public String getUsername() {
		return getMemberName();
	}

	@Override
	public String getPassword() {
		return getMemberPassword();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>() {
			{
				add(new SimpleGrantedAuthority(getMemberRole()));
			}
		};
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
