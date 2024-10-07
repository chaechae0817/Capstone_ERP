package com.erp.techInovate.techInovate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
//        - resignation_id (기본키)
//        - employee_id (외래키, Employees 테이블)
//        - resignation_date (퇴사일)
//        - resignation_reason (퇴사 사유)
//        - last_position (퇴사 당시 직급)
//        - last_department (퇴사 당시 부서)
//        - severance_pay (퇴직금)
//        - notes (비고)
public class ResignationDTO {
    private Long employeeId; // 사원 ID
    private String resignationReason; // 퇴사 사유
    private String notes; // 비고
    private Double severancePay; // 퇴직금
}
