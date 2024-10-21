package com.erp.techInovate.techInovate.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransferDTO {
    private Long transferId;
    private Long employeeId;
    private String employeeName;

    private Long fromDepartmentId;
    private String fromDepartmentName;

    private Long toDepartmentId;
    private String toDepartmentName;

    private Long fromPositionId;
    private String fromPositionName;

    private Long toPositionId;
    private String toPositionName;

    private LocalDate transferDate;
    private String personnelAppointment; // 발령 구분 (부서 이동, 승진 등)
}