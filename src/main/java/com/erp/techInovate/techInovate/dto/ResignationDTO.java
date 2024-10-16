package com.erp.techInovate.techInovate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResignationDTO {
    private Long employeeId; // 사원 ID
    private String resignationReason; // 퇴사 사유
    private String notes; // 비고
    private Double severancePay; // 퇴직금
}
