package com.cibertec.ApiSentinela.family.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyGroupDto {
    private String groupCode; // e.g. "FAM-8392"
    private String groupName; // e.g. "Familia Martínez"
    private Long adminUserId;
    private String adminName;
    private LocalDateTime createdAt;
    private List<FamilyMemberDto> members;
}
