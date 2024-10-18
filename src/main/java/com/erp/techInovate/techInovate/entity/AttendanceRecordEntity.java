package com.erp.techInovate.techInovate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "attendance_record")
public class AttendanceRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false) // 외래키 설정
    private EmployeeEntity employee; // 외래키 (Employees 테이블)

    private LocalDate date; // 근태 날짜

    private LocalTime checkInTime; // 출근 시간

    private LocalTime checkOutTime; // 퇴근 시간

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false) // 외래키 설정
    private AttendanceEntity attendance; // 외래키 (Attendance_Codes 테이블)

    private String notes; // 비고
}
