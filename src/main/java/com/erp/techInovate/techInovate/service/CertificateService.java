package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.dto.CareerCertificateDTO;
import com.erp.techInovate.techInovate.entity.*;
import com.erp.techInovate.techInovate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class CertificateService {

    @Autowired
    private static EmployeeRepository employeeRepository;
    @Autowired
    private static CompanyRepository companyRepository;
    @Autowired
    private static PositionRepository positionRepository;
    @Autowired
    private static DepartmentRepository departmentRepository;
    @Autowired
    private static ResignationRepository resignationRepository;

    public static CareerCertificateDTO getCareerCertificate(Long employeeId) {
        CareerCertificateDTO certificateDTO = new CareerCertificateDTO();

        // Employee 정보 조회
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        certificateDTO.setName(employee.getName());
        certificateDTO.setBirth_date(employee.getBirthDate().toString());
        certificateDTO.setContact_info(employee.getContactInfo());
        certificateDTO.setHireDate(employee.getHireDate().toString());

        // Company 정보 조회
        CompanyEntity company = companyRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        certificateDTO.setCompany_name(company.getName());
        certificateDTO.setBusiness_registration_number(company.getRegistrationNumber());
        certificateDTO.setRepresentative_name(company.getRepresentativeName());
        certificateDTO.setIndustry(company.getBusinessType());
        certificateDTO.setBusiness_loaction(company.getAddress());

        // Department 정보 조회
        DepartmentEntity department = departmentRepository.findById(employee.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        certificateDTO.setDepartment(department.getName());

        // Position 정보 조회
        PositionEntity position = positionRepository.findById(employee.getPosition().getId())
                .orElseThrow(() -> new RuntimeException("Position not found"));
        certificateDTO.setPosition(position.getName());

        // Resignation 정보 조회
        ResignationEntity resignation = resignationRepository.findById(employee.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Resignation not found"));
        certificateDTO.setResignation_reason(resignation.getResignationReason());


        // 재직기간 계산
        // 예: 입사일과 발급일을 기반으로 계산
        certificateDTO.setEmployment_period(calculateEmploymentPeriod(employee.getHireDate()));

        // 발급일 설정
        certificateDTO.setIssue_date(LocalDate.now().toString());

        return certificateDTO;
    }

    private static String calculateEmploymentPeriod(LocalDate hireDate) {
        // 입사일과 현재 날짜를 비교하여 재직 기간 계산
        Period period = Period.between(hireDate, LocalDate.now());
        return period.getYears() + "년 " + period.getMonths() + "개월";
    }
}
