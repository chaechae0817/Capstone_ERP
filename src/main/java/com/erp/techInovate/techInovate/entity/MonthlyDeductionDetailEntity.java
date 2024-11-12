package com.erp.techInovate.techInovate.entity;

import com.erp.techInovate.techInovate.entity.MonthlyDeductionSummaryEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "monthly_deduction_detail")
public class MonthlyDeductionDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "deduction_summary_id", nullable = false)
    private MonthlyDeductionSummaryEntity monthlyDeductionSummary;

    @Column(name = "deduction_name", nullable = false)
    private String deductionName; // 공제 항목 이름

    @Column(name = "deduction_amount", nullable = false)
    private Double deductionAmount; // 공제 금액
}
