package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.*;
import com.erp.techInovate.techInovate.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryCalculationService {

    private final EmployeeRepository employeeRepository;
    private final DeductionCodeRepository deductionCodeRepository;
    private final TaxBracketRepository taxBracketRepository;
    private final MonthlyDeductionSummaryRepository deductionSummaryRepository;
    private final MonthlyAttendanceSummaryRepository attendanceSummaryRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;


    @Transactional
    public void updateMonthlyDeductions(Long employeeId, YearMonth month) {
        // 직원 조회
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("직원을 찾을 수 없습니다."));

        // 기존 공제 내역 삭제
        deductionSummaryRepository.deleteByEmployeeAndMonth(employee, month.atDay(1));

        // 공제 내역 재계산 및 저장
        calculateAndSaveMonthlyDeductions(employeeId, month);
    }

    public double calculateThreeMonthAverageSalary(EmployeeEntity employee, LocalDate resignationDate) {
        YearMonth endMonth = YearMonth.from(resignationDate).minusMonths(1); // 퇴사 전 달까지 3개월
        YearMonth startMonth = endMonth.minusMonths(2);

        double totalSalary = 0.0;

        for (YearMonth month = startMonth; !month.isAfter(endMonth); month = month.plusMonths(1)) {
            double monthlySalary = calculateMonthlySalary(employee, month);
            totalSalary += monthlySalary;
        }

        // 3개월 평균 급여 계산
        double averageSalary = totalSalary / 3.0;

        // 천 원 단위로 잘라내기
        return Math.floor(averageSalary / 1000) * 1000;
    }

    // 월 급여 계산 로직 (기존에 사용 중이라면 재활용)
    private double calculateMonthlySalary(EmployeeEntity employee, YearMonth month) {
        double hourlyWage = calculateHourlyWage(employee.getSalary());
        double totalPaidHours = attendanceRecordRepository.findTotalPaidHoursByEmployeeAndMonth(employee.getEmployeeId(), month.atDay(1), month.atEndOfMonth()).orElse(0.0);
        return totalPaidHours * hourlyWage;
    }
    public void calculateAndSaveDeductionsForAllEmployees(YearMonth month) {
        LocalDate firstDayOfMonth = month.atDay(1);

        // 공제 내역이 없는 직원들만 필터링
        List<EmployeeEntity> employeesWithoutDeductions = employeeRepository.findAll().stream()
                .filter(employee -> !deductionSummaryRepository.existsByEmployeeAndMonth(employee, firstDayOfMonth))
                .collect(Collectors.toList());

        // 공제 내역이 없는 직원들에 대해서만 계산 및 저장
        for (EmployeeEntity employee : employeesWithoutDeductions) {
            calculateAndSaveMonthlyDeductions(employee.getEmployeeId(), month);
        }
    }


    //직원의 월 급여에 따른 총 공제 내역을 계산하고 저장
    public void calculateAndSaveMonthlyDeductions(Long employeeId, YearMonth month) {
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("직원을 찾을 수 없습니다."));

        LocalDate firstDayOfMonth = month.atDay(1);

        // 월 급여 계산
        double hourlyWage = calculateHourlyWage(employee.getSalary()); //15625
        double monthlySalary = getMonthlyPaidHours(employee, month) * hourlyWage; //3601563
        Integer family = employee.getFamily();
        if(family == null){
            family = 0;
        }

        // 소득세 계산
        double incomeTax = calculateIncomeTax(monthlySalary,family);
        System.out.println("소득세 : "+ incomeTax);
        // 공제 세부 내역 저장
        MonthlyDeductionSummaryEntity deductionSummary = new MonthlyDeductionSummaryEntity();
        deductionSummary.setEmployee(employee);
        deductionSummary.setMonth(firstDayOfMonth);

        List<MonthlyDeductionDetailEntity> deductionDetails = new ArrayList<>();
        double totalDeductions = incomeTax;

        // 소득세 세부 내역 추가
        deductionDetails.add(createDeductionDetail(deductionSummary, "소득세", incomeTax));

        // 기타 공제 항목 계산
        for (DeductionCodeEntity deductionCode : deductionCodeRepository.findAll()) {
            double deductionAmount = 0.0;

            switch (deductionCode.getCode()) {
                case "지방세":
                    deductionAmount = Math.round(incomeTax * (deductionCode.getPercentage() / 100.0));
                    break;

                case "국민연금":
                    deductionAmount = Math.round(monthlySalary * (deductionCode.getPercentage() / 100.0));
                    System.out.println("월급 : "+ monthlySalary);
                    System.out.println("국민연금%"+ deductionCode.getPercentage());
                    System.out.println("국민연금"+ deductionAmount);
                    break;

                case "건강보험료":
                    deductionAmount = Math.round(monthlySalary * (deductionCode.getPercentage() / 100.0));
                    break;

                case "고용보험료":
                    deductionAmount = Math.round(monthlySalary * (deductionCode.getPercentage() / 100.0));
                    break;

                case "장기요양보험료":
                    double healthInsurance = Math.round(monthlySalary * (deductionCodeRepository.findByCode("건강보험료")
                            .orElseThrow(() -> new IllegalArgumentException("건강보험료 코드가 존재하지 않습니다."))
                            .getPercentage() / 100.0));
                    deductionAmount = Math.round(healthInsurance * (deductionCode.getPercentage() / 100.0));
                    break;

                default:
                    deductionAmount = Math.round(monthlySalary * (deductionCode.getPercentage() / 100.0));
                    break;
            }


            deductionAmount = Math.round(deductionAmount / 10.0) * 10.0;
            // 총 공제액 누적
            totalDeductions += deductionAmount;

            // 공제 세부 내역 추가
            deductionDetails.add(createDeductionDetail(deductionSummary, deductionCode.getCode(), deductionAmount));
        }

        deductionSummary.setTotalDeductions(totalDeductions);
        deductionSummary.setDeductionDetails(deductionDetails);

        deductionSummaryRepository.save(deductionSummary);
    }

    /**
     * 소득세 계산
     */
    private double calculateIncomeTax(double monthlySalary, int familyCount) {
        // 가족 수와 연봉에 맞는 과세 구간을 찾음
        TaxBracketEntity taxBracket = taxBracketRepository.findTaxBySalaryAndDependents((long) monthlySalary, familyCount)
                .orElseThrow(() -> new IllegalArgumentException("해당 연봉과 가족 수에 맞는 과세 구간을 찾을 수 없습니다."));


        double annualIncomeTax = taxBracket.getSimplifiedTaxAmount();

        // 연간 소득세를 천 원 단위로 반올림하여 월 소득세로 반환
        return annualIncomeTax;
    }


    /**
     * 시급 계산
     */
    private double calculateHourlyWage(double annualSalary) {
        double monthlyBaseSalary = (annualSalary / 12.0);
        double dailyWage = (monthlyBaseSalary / 20.0);
        return Math.round(dailyWage/8.0);
    }

    /**
     * 유급 근무 시간 조회
     */
    private double getMonthlyPaidHours(EmployeeEntity employee, YearMonth month) {
        return attendanceSummaryRepository.findByEmployeeAndMonth(employee, month.atDay(1))
                .map(MonthlyAttendanceSummaryEntity::getTotalPaidWorkHours)
                .orElse(0.0); // 근태 요약이 없으면 기본값 0.0 반환
    }



    /**
     * 공제 세부 내역 생성
     */
    private MonthlyDeductionDetailEntity createDeductionDetail(MonthlyDeductionSummaryEntity summary, String deductionName, double deductionAmount) {
        MonthlyDeductionDetailEntity detail = new MonthlyDeductionDetailEntity();
        detail.setMonthlyDeductionSummary(summary);
        detail.setDeductionName(deductionName);
        detail.setDeductionAmount(deductionAmount);
        return detail;
    }

    //천원 단위로 반올림
    private double roundToNearestThousand(double amount) {
        return Math.round(amount / 1000.0) * 1000.0;
    }

    public List<MonthlyDeductionSummaryEntity> getMonthlyDeductionsForAllEmployees(YearMonth month) {
        LocalDate firstDayOfMonth = month.atDay(1);
        return deductionSummaryRepository.findByMonth(firstDayOfMonth);
    }


}
