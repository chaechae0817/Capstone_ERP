package com.erp.techInovate.techInovate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Getter
@Setter
@Table(name = "monthly_attendance_summary")
public class MonthlyAttendanceSummaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 ID

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee; // 직원
    private LocalDate month; // 해당 월

    private Double normalWorkDays; // 정상 근무 일수
    private Double halfWorkDays; // 부분 근무 일수 (0.5일 단위)
    private Double totalWorkDays; // 총 근무 일수 (정상 근무 일수 + 부분 근무 일수)
    private Double totalAbsenceDays; // 결근 일수
    private Double totalWorkHours; // 총 근무 시간
    private Double totalPaidWorkHours; // 급여가 지급되는 근무 시간 (유급 조퇴 등 포함)
    private Double specialWorkDays; // 출장 등 특수 근무 일수
    private Double overtimeHours; // 야근 시간 (18시 이후)
    private Double holidayWorkDays; // 휴일/공휴일 근무 일수
}
