package com.erp.techInovate.techInovate.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class VacationDTO
{
    private Long vacationId; // 휴가 ID
    private Long employeeId; // 사원 ID
    private String vacationTypeName; // 휴가 종목 이름
    private LocalDate startDate; // 시작일
    private LocalDate endDate; // 종료일
    private String reason; // 휴가 사유
}
