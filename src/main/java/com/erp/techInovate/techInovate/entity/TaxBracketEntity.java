package com.erp.techInovate.techInovate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="TaxBracket")
public class TaxBracketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long minSalary; // 월 급여 구간 최소

    @Column(nullable = true)
    private Long maxSalary; // 월 급여 구간 최대 (무한대일 경우 null)

    @Column(nullable = false)
    private int dependentCount; // 부양가족 수

    @Column(nullable = false)
    private Long simplifiedTaxAmount; // 간이세액 (고정)
}
