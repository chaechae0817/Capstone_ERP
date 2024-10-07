package com.erp.techInovate.techInovate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
//재직 증명서용 DTO
public class EmployeeCertificateDTO {
    private String name;            //이름
    private String birth_date;      //성명
    private String address;         //주소
    private String company_name;    //회사 이름
    private String business_registration_number;    //사업자등록번호
    private String representative_name; //대표명
    private String industry;    //업종
    private String business_loaction;   //사업장 소재지
    private String department;  //근무 부서
    private String position;    //직위
    private String hireDate;    //입사일
    private String purpose; //용도
    private String issue_date; //발급일
}
