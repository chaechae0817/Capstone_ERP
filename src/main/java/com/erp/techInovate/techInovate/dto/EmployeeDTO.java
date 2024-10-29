package com.erp.techInovate.techInovate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EmployeeDTO {
    private Long employeeId; // 기본키
    private String employeeNumber; // 사원번호 (자동 생성)
    private String name; // 이름
    private String ssn; // 주민번호
    private Long positionId; // 직급
    private String status; // 직원 구분 (정규, 계약, 퇴사)
    private Long departmentId; // 부서
    private LocalDate hireDate; // 입사일
    private String contactInfo; // 연락처
    private String email; // 이메일
    private LocalDate birthDate; // 생년월일
    private String address; // 주소
    private String experience; // 경력
    private String accountNumber; // 계좌 번호
    private String bank; // 은행
    private MultipartFile photo; //사진
    private Double salary; // 추가된 연봉 필드


}
