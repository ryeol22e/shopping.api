package com.project.shopping.common.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Entity
@Table(name = "COMMON_FIELD")
@NoArgsConstructor
public class CodeFieldDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codeId;
	private String codeType;
	private Integer codeDepth;
	private String codeName;
	private String addInfo1;
	private String addInfo2;
}
