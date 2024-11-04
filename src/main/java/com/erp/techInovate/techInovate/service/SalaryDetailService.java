package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.AllowanceTotalEntity;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.MonthlyDeductionDetailEntity;
import com.erp.techInovate.techInovate.entity.MonthlyDeductionSummaryEntity;
import com.erp.techInovate.techInovate.repository.AllowanceTotalRepository;
import com.erp.techInovate.techInovate.repository.AttendanceRecordRepository;
import com.erp.techInovate.techInovate.repository.EmployeeRepository;
import com.erp.techInovate.techInovate.repository.MonthlyDeductionSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SalaryDetailService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final AllowanceTotalRepository allowanceTotalRepository;
    private final MonthlyDeductionSummaryRepository deductionSummaryRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * 특정 직원의 월 급여 정보를 계산하고 가져오는 메서드
     */
    public List<Map<String, Object>> getMonthlySalaryDetailsForAllEmployees(YearMonth month) {
        List<Map<String, Object>> allEmployeeSalaryDetails = new ArrayList<>();

        // 전체 직원 조회
        List<EmployeeEntity> allEmployees = employeeRepository.findAll();

        for (EmployeeEntity employee : allEmployees) {
            Map<String, Object> salaryDetails = new LinkedHashMap<>();
            Long employeeId = employee.getEmployeeId();

            // 1. 시급과 월 급여 계산
            double hourlyWage = calculateHourlyWage(employee.getSalary());
            double totalPaidHours = getTotalPaidHours(employeeId, month).orElse(0.0);
            double monthlySalary = roundToNearestThousand(totalPaidHours * hourlyWage);

            // 2. 추가 수당 정보
            AllowanceTotalEntity allowanceTotal = allowanceTotalRepository.findByEmployeeAndMonth(employee, month.atDay(1)).orElse(null);
            double totalAllowance = allowanceTotal != null ? allowanceTotal.getTotalAllowance() : 0.0;
            Map<String, Double> allowanceDetails = allowanceTotal != null ? allowanceTotal.getAllowanceDetails() : new LinkedHashMap<>();

            // 3. 총 공제 사항
            MonthlyDeductionSummaryEntity deductionSummary = deductionSummaryRepository.findByEmployeeAndMonth(employee, month.atDay(1)).orElse(null);
            double totalDeductions = deductionSummary != null ? deductionSummary.getTotalDeductions() : 0.0;
            Map<String, Double> deductionDetails = deductionSummary != null ? deductionSummary.getDeductionDetails().stream()
                    .collect(Collectors.toMap(
                            MonthlyDeductionDetailEntity::getDeductionName,
                            MonthlyDeductionDetailEntity::getDeductionAmount,
                            (existing, replacement) -> existing, LinkedHashMap::new)) : new LinkedHashMap<>();

            // 데이터 매핑
            salaryDetails.put("employeeName", employee.getName());
            salaryDetails.put("monthlySalary", monthlySalary);
            salaryDetails.put("hourlyWage", hourlyWage);
            salaryDetails.put("totalPaidHours", totalPaidHours);
            salaryDetails.put("totalAllowance", totalAllowance);
            salaryDetails.put("allowanceDetails", allowanceDetails);
            salaryDetails.put("totalDeductions", totalDeductions);
            salaryDetails.put("deductionDetails", deductionDetails);

            allEmployeeSalaryDetails.add(salaryDetails);
        }

        return allEmployeeSalaryDetails;
    }

    // 추가 수당 항목 헤더 목록 가져오기
    public Set<String> getAllowanceHeaders() {
        return allowanceTotalRepository.findAll().stream()
                .flatMap(allowanceTotal -> allowanceTotal.getAllowanceDetails().keySet().stream())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

     //공제 항목 헤더 목록 가져오기
    public Set<String> getDeductionHeaders() {
        return deductionSummaryRepository.findAll().stream()
                .flatMap(deductionSummary -> deductionSummary.getDeductionDetails().stream()
                        .map(MonthlyDeductionDetailEntity::getDeductionName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private double calculateHourlyWage(double annualSalary) {
        double monthlyBaseSalary = annualSalary / 12.0;
        double dailyWage = monthlyBaseSalary / 20.0;
        return Math.round(dailyWage / 8.0);
    }

    private double roundToNearestThousand(double amount) {
        return Math.round(amount / 1000.0) * 1000.0;
    }
    public Optional<Double> getTotalPaidHours(Long employeeId, YearMonth month) {
        LocalDate startOfMonth = month.atDay(1);
        LocalDate endOfMonth = month.atEndOfMonth();
        return attendanceRecordRepository.findTotalPaidHoursByEmployeeAndMonth(employeeId, startOfMonth, endOfMonth);
    }
}
