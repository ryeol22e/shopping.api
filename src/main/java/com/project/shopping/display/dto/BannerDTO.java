package com.project.shopping.display.dto;

import java.sql.Blob;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@ToString
@IdClass(value = BannerDTO.class)
@Table(name = "BANNER_TABLE")
@NoArgsConstructor
@JsonIgnoreProperties
public class BannerDTO {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String bannerType;
	private Blob imageUrl;
	private String title;
	private String description;
	private String linkUrl;
	private char dispYn;
	private char useYn;
	private LocalDateTime dispStartDtm;
	private LocalDateTime dispEndDtm;
	private LocalDateTime rgstDtm;
	private LocalDateTime modDtm;
}
