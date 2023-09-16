package com.project.shopping.common.repository;

import static com.project.shopping.common.dto.QCodeFieldDTO.codeFieldDTO;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.project.shopping.common.dto.CodeFieldDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommonRepository {
	private final JPAQueryFactory factory;

	public List<CodeFieldDTO> findByCommonCodeList(CodeFieldDTO dto) {
		return factory.selectFrom(codeFieldDTO)
			.where(codeFieldDTO.codeType.eq(dto.getCodeType())
				.and(codeFieldDTO.codeDepth.eq(dto.getCodeDepth())
				.and(codeFieldDTO.useYn.eq(dto.getUseYn()))))
			.fetch();
	}
}
