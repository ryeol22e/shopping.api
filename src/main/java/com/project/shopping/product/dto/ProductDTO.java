package com.project.shopping.product.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "PRODUCT_TABLE")
@NoArgsConstructor
@JsonIgnoreProperties(value = {}, allowSetters = true)
public class ProductDTO {
	@Id
	private Long prdtNo;
	private String prdtName;
	private char dispYn;
	private char useYn;
	private String normalPrice;
	private String sellPrice;
	@CreationTimestamp
	private LocalDateTime regDtime;
	@UpdateTimestamp
	private LocalDateTime updateDtime;
}
