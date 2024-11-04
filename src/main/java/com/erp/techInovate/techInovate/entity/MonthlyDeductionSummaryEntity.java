package com.erp.techInovate.techInovate.entity;

import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "monthly_deduction_summary")
public class MonthlyDeductionSummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @Column(name = "month", nullable = false)
    private LocalDate month; // 월별 저장 (yyyy-MM-dd 형식, 첫째 날 사용)

    @Column(name = "total_deductions")
    private Double totalDeductions; // 총 공제액

    @OneToMany(mappedBy = "monthlyDeductionSummary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonthlyDeductionDetailEntity> deductionDetails; // 공제 세부 내역
}
