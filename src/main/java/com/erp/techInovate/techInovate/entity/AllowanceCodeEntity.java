package com.erp.techInovate.techInovate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "allowance_code")
public class AllowanceCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allowanceCodeId;

    private String code; // 수당 코드 (예: 직책수당, 기술수당)

    private String description; // 설명

    @Enumerated(EnumType.STRING)
    private CalculationType calculationType; // 계산 방식 (고정 금액, 비율, 일일 계산 등)

    private Double fixedAmount; // 고정 금액

    private Double dailyRate; // 일일 수당 금액

    private Double percentage; // 기본 급여에 대한 비율 (예: 성과금 10%)
}
