package com.erp.techInovate.techInovate.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class TransferDTO {
    private Long transferId;
    private Long employeeId;
    private String personnelAppointment;
    private String fromDepartment;
    private String toDepartment;
    private String fromPosition;
    private String toPosition;
    private LocalDate transferDate;


}
