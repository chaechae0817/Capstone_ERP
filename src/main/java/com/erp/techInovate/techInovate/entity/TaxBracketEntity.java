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
    private Long minIncome;  //연봉 최솟값 기준

    @Column
    private Long maxIncome; //연봉 최댓값 기준

    @Column(nullable = false)
    private Long fixedAmount; //고정 새액

    @Column(nullable = false)
    private Double rate; //초과 금액에 대한 비율 (%)
}
