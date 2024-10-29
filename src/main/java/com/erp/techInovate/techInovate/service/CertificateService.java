package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.dto.CareerCertificateDTO;
import com.erp.techInovate.techInovate.dto.EmployeeCertificateDTO;
import com.erp.techInovate.techInovate.entity.*;
import com.erp.techInovate.techInovate.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final ResignationRepository resignationRepository;

    public CareerCertificateDTO getCareerCertificate(Long employeeId) {
        CareerCertificateDTO certificateDTO = new CareerCertificateDTO();

        // Employee 정보 조회
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        certificateDTO.setName(employee.getName());
        certificateDTO.setBirth_date(employee.getBirthDate().toString());
        certificateDTO.setContact_info(employee.getContactInfo());
        certificateDTO.setHireDate(employee.getHireDate().toString());

        // Company 정보 조회
        CompanyEntity company = companyRepository.findById(1L) // 회사 ID는 적절히 수정하세요
                .orElseThrow(() -> new RuntimeException("Company not found"));

        certificateDTO.setCompany_name(company.getName());
        certificateDTO.setBusiness_registration_number(company.getRegistrationNumber());
        certificateDTO.setRepresentative_name(company.getRepresentativeName());
        certificateDTO.setIndustry(company.getBusinessType());
        certificateDTO.setBusiness_location(company.getAddress());

        // Department 정보 조회
        DepartmentEntity department = departmentRepository.findById(employee.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        certificateDTO.setDepartment(department.getName());

        // Position 정보 조회
        PositionEntity position = positionRepository.findById(employee.getPosition().getId())
                .orElseThrow(() -> new RuntimeException("Position not found"));

        certificateDTO.setPosition(position.getName());

        // Resignation 정보 조회
        List<ResignationEntity> resignations = resignationRepository.findByEmployee_EmployeeId(employeeId);
        if (!resignations.isEmpty()) {
            ResignationEntity resignation = resignations.get(0);
            certificateDTO.setResignation_reason(resignation.getResignationReason());
        } else {
            certificateDTO.setResignation_reason("재직 중인 인원");
        }
        // 재직기간 계산
        certificateDTO.setEmployment_period(calculateEmploymentPeriod(employee.getHireDate()));

        // 발급일 설정
        certificateDTO.setIssue_date(LocalDate.now().toString());

        return certificateDTO;
    }

    private String calculateEmploymentPeriod(LocalDate hireDate) {
        // 입사일과 현재 날짜를 비교하여 재직 기간 계산
        Period period = Period.between(hireDate, LocalDate.now());
        return period.getYears() + "년 " + period.getMonths() + "개월";
    }

    public EmployeeCertificateDTO getEmployeeCertificate(Long employeeId,String purpose) {
        EmployeeCertificateDTO employeeDTO = new EmployeeCertificateDTO();
        // Employee 정보 조회
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employeeDTO.setName(employee.getName());
        employeeDTO.setBirth_date(employee.getBirthDate().toString());
        employeeDTO.setContact_info(employee.getContactInfo());
        employeeDTO.setAddress(employee.getAddress());
        employeeDTO.setHireDate(employee.getHireDate().toString());

        // Company 정보 조회
        CompanyEntity company = companyRepository.findById(1L) // 회사 ID는 적절히 수정하세요
                .orElseThrow(() -> new RuntimeException("Company not found"));

        employeeDTO.setCompany_name(company.getName());
        employeeDTO.setBusiness_registration_number(company.getRegistrationNumber());
        employeeDTO.setRepresentative_name(company.getRepresentativeName());
        employeeDTO.setIndustry(company.getBusinessType());
        employeeDTO.setBusiness_location(company.getAddress());

        // Department 정보 조회
        DepartmentEntity department = departmentRepository.findById(employee.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        employeeDTO.setDepartment(department.getName());

        // Position 정보 조회
        PositionEntity position = positionRepository.findById(employee.getPosition().getId())
                .orElseThrow(() -> new RuntimeException("Position not found"));

        employeeDTO.setPosition(position.getName());

        //용도 설정
        employeeDTO.setPurpose(purpose); // 선택된 용도를 설정

        // 발급일 설정
        employeeDTO.setIssue_date(LocalDate.now().toString());

        return employeeDTO;
    }



}
