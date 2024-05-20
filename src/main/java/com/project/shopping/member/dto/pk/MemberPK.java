package com.project.shopping.member.dto.pk;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class MemberPK implements Serializable {
    private Long memberNo;
    private String memberId;
}
