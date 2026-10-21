package com.cibertec.ApiSentinela.family.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinFamilyGroupRequest {
    private Long userId;
    private String groupCode;
}
