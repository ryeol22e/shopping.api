package com.project.shopping.common.dto;

import java.io.Serializable;
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
@Table
@NoArgsConstructor
public class CommonField implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codeId;
	private String codeType;
	private Integer codeDepth;
	private String codeName;
	private char useYn;
	private String addInfo1;
	private String addInfo2;
}
