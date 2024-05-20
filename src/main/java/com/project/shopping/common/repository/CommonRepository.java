package com.project.shopping.common.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.project.shopping.common.dto.CommonField;
import com.project.shopping.common.dto.QCommonField;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommonRepository {
    @Qualifier(value = "mariadbFactory")
    private final JPAQueryFactory mariadbFactory;

    public List<CommonField> findByCommonCodeList(CommonField dto) {
        QCommonField commonField = QCommonField.commonField;
        return mariadbFactory.selectFrom(commonField)
                .where(commonField.codeType.eq(dto.getCodeType()),
                        commonField.codeDepth.eq(dto.getCodeDepth()),
                        commonField.useYn.eq(dto.getUseYn()))
                .fetch();
    }
}
