package com.project.shopping.display.dto;

import com.project.shopping.display.dto.pk.CategoryPK;

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
@ToString
@Entity
@IdClass(CategoryPK.class)
@Table(name = "STANDARD_CATEGORY")
@NoArgsConstructor
public class CategoryDTO {
	@Id
	@Column(name = "CATE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cateId;
	@Id
	@Column(name = "CATE_NO")
	private String cateNo;
	private String cateDepth;
	private String upCateNo;
	private String cateName;
	private String cateFullPath;
	private char useYn;
	private char dispYn;
}
