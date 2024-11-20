package com.erp.techInovate.techInovate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResignationDTO {
    private Long employeeId;         // 사원 ID
    private String name;             // 사원 이름
    private String contactInfo;      // 전화번호
    private String position;         // 직급
    private String department;       // 부서
    private LocalDate resignationDate; // 퇴사일
    private LocalDate hireDate;        // 입사일
}
