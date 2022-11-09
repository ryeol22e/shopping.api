package com.project.shopping.product.dto;

import java.sql.Blob;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.shopping.product.dto.pk.ProductPK;

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
import lombok.NonNull;

@Getter
@Entity
@IdClass(value = ProductPK.class)
@Table(name = "PRODUCT_TABLE")
@NoArgsConstructor
@JsonIgnoreProperties(value = {"imageData"}, allowSetters = true)
public class ProductDTO {
	@Id
	@Column(name = "PRDT_INDEX")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long prdtIndex;
	@Id
	@Column(name = "PRDT_NO")
	private String prdtNo;
	private String cateNo;
	private String prdtName;
	private char dispYn;
	private char useYn;
	private String normalPrice;
	private String sellPrice;
	@NonNull
	private Blob imageData;
	@Transient
	private byte[] image;
	@CreationTimestamp
	private LocalDateTime regDtime;
	@UpdateTimestamp
	private LocalDateTime updateDtime;
	@Transient
	MultipartFile file;

	public void setImageData(Blob blob) {
		this.imageData = blob;
	}

	public void setImage(byte[] bytes) {
		this.image = bytes;
	}
}
