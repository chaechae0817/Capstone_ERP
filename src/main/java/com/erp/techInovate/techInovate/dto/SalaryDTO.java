package com.erp.techInovate.techInovate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;


@Getter
@Setter
@ToString
public class SalaryDTO {
    private Long employeeId;
    private String employeeName;
    private String monthlySalary; // 월 기본급
    private Map<String, Double> allowanceDetails; // 추가 수당 세부 내역
    private double totalAllowance; // 총 추가 수당
    private Map<String, Double> deductionDetails; // 총 공제 세부 내역
    private double totalDeductions; // 총 공제 금액
}
