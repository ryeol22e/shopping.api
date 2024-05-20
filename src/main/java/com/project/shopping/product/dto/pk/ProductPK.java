package com.project.shopping.product.dto.pk;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class ProductPK implements Serializable {
    private Long prdtIndex;
    private String prdtNo;
}
