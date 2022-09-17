package com.project.shopping.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class MemberDTO {
    private String userId;
    private String userEmail;
    private String userName;
    
}
