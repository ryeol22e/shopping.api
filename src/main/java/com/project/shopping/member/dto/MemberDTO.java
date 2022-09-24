package com.project.shopping.member.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Getter
@Entity
@Table(name = "MEMBER_TABLE")
@JsonIgnoreProperties(value = {"changeBcryptPassword", "memberPassword", "updateLoginDtm"}, allowSetters = true)
@ToString
@NoArgsConstructor
public class MemberDTO {
    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;
    @Id
    @NonNull
    private String memberId;
    @NonNull
    private String memberPassword;
    @NonNull
    private String memberEmail;
    @NonNull
    private String memberName;
    private String memberAddr;
    private String memberToken;
    private String refreshToken;
    @CreationTimestamp
    private LocalDateTime joinDtm = LocalDateTime.now();
    @UpdateTimestamp
    private LocalDateTime loginDtm;

    public void changeBcryptPassword() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		this.memberPassword = encoder.encode(getMemberPassword());
	}

	public void updateLoginDtm() {
		this.loginDtm = LocalDateTime.now();
	}
    
}
