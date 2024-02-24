package com.project.shopping.member.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.shopping.member.dto.pk.MemberPK;
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
@Table
@ToString
@NoArgsConstructor
// setters response 보낼때 제외시킴, getters request 받을때 제외
@JsonIgnoreProperties(
		value = {"changeBcryptPassword", "memberPassword", "setAuthNumber", "refreshToken"},
		allowSetters = true)
public class MemberInfo implements UserDetails {

	public MemberInfo(Long memberNo, String memberName, String memberRole) {
		this.memberNo = memberNo;
		this.memberName = memberName;
		this.memberRole = memberRole;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberNo;
	@Id
	private String memberId;
	private String memberPassword;
	private String memberEmail;
	private String memberName;
	private String memberAddr;
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

	public void clearPassword() {
		this.memberPassword = null;
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
		return List.of(new SimpleGrantedAuthority(getMemberRole()));
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
