package com.project.shopping.display.dto;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.shopping.display.dto.pk.BannerPK;
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
@ToString
@IdClass(value = BannerPK.class)
@Table(name = "BANNER_TABLE")
@NoArgsConstructor
@JsonIgnoreProperties(value = {"imageData"}, allowSetters = true)
public class BannerDTO {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String bannerType;
	private Blob imageData;
	private String title;
	private String description;
	private char dispYn;
	private char useYn;
	private LocalDateTime dispStartDtm;
	private LocalDateTime dispEndDtm;
	@CreationTimestamp
	private LocalDateTime rgstDtm;
	@UpdateTimestamp
	private LocalDateTime modDtm;

	@Transient
	private String dispStart;
	@Transient
	private String dispEnd;
	@Transient
	private MultipartFile file;
	@Transient
	private byte[] image;

	public void setImageData(Blob data) {
		this.imageData = data;
	}

	public void setImage(byte[] bytes) {
		this.image = bytes;
	}

	public void setDispDate() {
		this.dispStartDtm =
				LocalDateTime.of(LocalDate.parse(this.dispStart), LocalTime.of(00, 00, 00));
		this.dispEndDtm = LocalDateTime.of(LocalDate.parse(this.dispEnd), LocalTime.of(23, 59, 59));
	}
}
