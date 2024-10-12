package com.erp.techInovate.techInovate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
//경력 증명서
public class CareerCertificateDTO {
    private String name; //이름 - employee
    private String birth_date; //생년월일 - employee
    private String contact_info; //연락처 - employee
    private String company_name;    //회사 이름 - company
    private String business_registration_number;    //사업자등록번호 -company
    private String representative_name; //대표명 - company
    private String industry;    //업종 - company
    private String business_loaction;   //사업장 소재지 - company
    private String department;  //근무 부서 - Department 테이블
    private String position;    //직위 - Position 테이블
    private String hireDate; //입사일 - employee
    private String employment_period; //재직기간 ( 발급일 - 입사일 계산)
    private String resignation_reason; //퇴사 사유 - resignation
    private String issue_date; //발급일
}
