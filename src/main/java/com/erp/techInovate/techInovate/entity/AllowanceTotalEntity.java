package com.erp.techInovate.techInovate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name = "allowance_total")
public class AllowanceTotalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allowanceTotalId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @Column(name = "month", columnDefinition = "DATE")
    private LocalDate month;

    private Double basicSalary; // 월 기본 급여

    private Double totalAllowance; // 총 수당

    @ElementCollection
    @CollectionTable(name = "allowance_detail", joinColumns = @JoinColumn(name = "allowance_total_id"))
    @MapKeyColumn(name = "allowance_code")
    @Column(name = "amount")
    private Map<String, Double> allowanceDetails; // 수당 내역 (코드:금액)
}
