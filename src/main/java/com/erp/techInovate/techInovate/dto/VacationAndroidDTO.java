package com.erp.techInovate.techInovate.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VacationAndroidDTO {
    private Long id;
    private Long employeeId;       // 사원 ID
    private String name;           // 사원 이름
    private String position;       // 직급
    private String department;     // 부서
    private String leaveItemName;      // 선택한 휴가 항목 ID
    private String startDate;   // 시작일
    private String endDate;     // 종료일
    private String reason;         // 신청 사유
}
