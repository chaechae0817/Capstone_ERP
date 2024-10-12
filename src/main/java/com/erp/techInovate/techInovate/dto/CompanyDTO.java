package com.erp.techInovate.techInovate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanyDTO {
    private Long id;                     // 기본키
    private String name;                 // 회사 이름
    private String address;              // 회사 주소
    private String contactNumber;        // 회사 연락처
    private String email;                // 회사 이메일
    private String registrationNumber;    // 사업자 등록번호
    private String representativeName;    // 대표명
    private String businessType;          // 업종
}
