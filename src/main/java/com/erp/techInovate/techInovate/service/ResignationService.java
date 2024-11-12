package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.ResignationEntity;
import com.erp.techInovate.techInovate.repository.EmployeeRepository;
import com.erp.techInovate.techInovate.repository.ResignationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResignationService {
    @Autowired
    private final ResignationRepository resignationRepository;
    private final EmployeeRepository employeeRepository;
    private final SalaryCalculationService salaryCalculationService;

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
}

