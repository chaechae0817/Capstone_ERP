package com.erp.techInovate.techInovate.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Setter
public class AttendanceRecordDTO {
    private Long recordId;
    private Long employeeId;
    private String employeeName;
    private String date;
    private String checkInTime;
    private String checkOutTime;
    private String attendanceType;
    private String notes;
    private String totalWorkHours;
}
