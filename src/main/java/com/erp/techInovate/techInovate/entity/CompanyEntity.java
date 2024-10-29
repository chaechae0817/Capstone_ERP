package com.erp.techInovate.techInovate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Company")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키

    private String name;                // 회사 이름
    private String address;             // 사업장 소재지
    private String contactNumber;       // 회사 연락처
    private String email;               // 회사 이메일
    private String registrationNumber;   // 사업자 등록번호
    private String representativeName;   // 대표명
    private String businessType;         // 업종
    private String corporateNumber;     // 법인 번호 추가

}
