package com.erp.techInovate.techInovate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="Employee_Resignations")
public class ResignationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resignationId; // 기본키

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee; // 외래키, Employees 테이블

    private LocalDate resignationDate; // 퇴사일
    private String resignationReason; // 퇴사 사유
    private String lastPosition; // 퇴사 당시 직급
    private String lastDepartment; // 퇴사 당시 부서
    private Double severancePay; // 퇴직금
    private String notes; // 비고
}