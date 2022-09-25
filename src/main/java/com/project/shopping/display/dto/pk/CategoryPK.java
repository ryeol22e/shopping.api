package com.project.shopping.display.dto.pk;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryPK implements Serializable {
	private Long cateId;
	private String cateNo;
}
