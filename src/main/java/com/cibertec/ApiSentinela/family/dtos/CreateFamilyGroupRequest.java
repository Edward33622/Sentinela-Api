package com.cibertec.ApiSentinela.family.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFamilyGroupRequest {
    private Long userId;
    private String groupName;
}
