package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.dto.ResignationDTO;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.ResignationEntity;
import com.erp.techInovate.techInovate.repository.DepartmentRepository;
import com.erp.techInovate.techInovate.repository.EmployeeRepository;
import com.erp.techInovate.techInovate.repository.PositionRepository;
import com.erp.techInovate.techInovate.repository.ResignationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResignationService {
    @Autowired
    private final ResignationRepository resignationRepository;
    private final EmployeeRepository employeeRepository;
    private final SalaryCalculationService salaryCalculationService;

    @Autowired
    private final DepartmentRepository departmentRepository;
    @Autowired
    private final PositionRepository positionRepository;
    public void save(ResignationEntity resignation) {
        resignationRepository.save(resignation);
    }

    // 퇴사 승인 처리 및 퇴직금 계산 후 업데이트
    public void approveResignation(Long id, String notes) {
        ResignationEntity resignation = resignationRepository.findById(id).orElseThrow();
        EmployeeEntity employee = resignation.getEmployee();

        // 퇴직금 계산
        double severancePay = calculateSeverancePay(employee, resignation.getResignationDate());
        resignation.setSeverancePay(severancePay); // 퇴직금 설정
        resignation.setNotes(notes);

        resignationRepository.save(resignation); // 승인 및 퇴직금 저장
    }

    // 3개월 급여 평균 계산 메서드
    public double getThreeMonthAverageSalary(EmployeeEntity employee, LocalDate resignationDate) {
        return salaryCalculationService.calculateThreeMonthAverageSalary(employee, resignationDate);
    }

    public void rejectResignation(Long id) {
        resignationRepository.deleteById(id); // 거부 처리
    }

    public Optional<ResignationEntity> findById(Long id){
        return resignationRepository.findByResignationId(id);
    }

    public List<ResignationEntity> findAll() {
        return resignationRepository.findAll(); // 모든 퇴사자 목록 조회
    }

    // 퇴직금 계산 로직
    public double calculateSeverancePay(EmployeeEntity employee, LocalDate resignationDate) {
        // 퇴사일 이전 3개월간의 급여 평균을 구합니다.
        double threeMonthAverageSalary = salaryCalculationService.calculateThreeMonthAverageSalary(employee, resignationDate);

        // 근속 일수 계산 (입사일부터 퇴사일까지)
        long tenureDays = ChronoUnit.DAYS.between(employee.getHireDate(), resignationDate);

        // 퇴직금 계산 공식 적용
        return threeMonthAverageSalary / 90 * 30 * (tenureDays / 365.0);
    }
    public List<ResignationDTO> searchResignedEmployees(
            String employeeName, String contactInfo, String position, String department,
            LocalDate startDate, LocalDate endDate) {

        List<ResignationEntity> resignations = resignationRepository.findAll();

        // 이름 필터링
        if (employeeName != null && !employeeName.isEmpty()) {
            resignations = resignations.stream()
                    .filter(r -> r.getEmployee().getName().contains(employeeName))
                    .collect(Collectors.toList());
        }

        // 전화번호 필터링
        if (contactInfo != null && !contactInfo.isEmpty()) {
            resignations = resignations.stream()
                    .filter(r -> r.getEmployee().getContactInfo().contains(contactInfo))
                    .collect(Collectors.toList());
        }

        // 직급 필터링
        if (position != null && !position.isEmpty()) {
            resignations = resignations.stream()
                    .filter(r -> r.getLastPosition().getName().equals(position))
                    .collect(Collectors.toList());
        }

        // 부서 필터링
        if (department != null && !department.isEmpty()) {
            resignations = resignations.stream()
                    .filter(r -> r.getLastDepartment().getName().equals(department))
                    .collect(Collectors.toList());
        }

        // 날짜 필터링
        if (startDate != null) {
            resignations = resignations.stream()
                    .filter(r -> !r.getResignationDate().isBefore(startDate))
                    .collect(Collectors.toList());
        }
        if (endDate != null) {
            resignations = resignations.stream()
                    .filter(r -> !r.getResignationDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        // DTO로 변환
        return resignations.stream().map(resignation -> {
            ResignationDTO dto = new ResignationDTO();
            dto.setResignationId(resignation.getResignationId());
            dto.setEmployee(resignation.getEmployee());
            dto.setLastPosition(resignation.getLastPosition());
            dto.setLastDepartment(resignation.getLastDepartment());
            dto.setResignationDate(resignation.getResignationDate());
            dto.setResignationReason(resignation.getResignationReason());
            dto.setSeverancePay(resignation.getSeverancePay());
            dto.setNotes(resignation.getNotes());

            return dto;
        }).collect(Collectors.toList());
    }
    public List<String> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> department.getName())
                .collect(Collectors.toList());
    }

    public List<String> getAllPositions() {
        return positionRepository.findAll().stream()
                .map(position -> position.getName())
                .collect(Collectors.toList());
    }
}

