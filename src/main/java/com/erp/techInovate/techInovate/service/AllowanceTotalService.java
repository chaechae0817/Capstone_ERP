package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.AllowanceCodeEntity;
import com.erp.techInovate.techInovate.entity.AllowanceTotalEntity;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.MonthlyAttendanceSummaryEntity;
import com.erp.techInovate.techInovate.repository.AllowanceCodeRepository;
import com.erp.techInovate.techInovate.repository.AllowanceTotalRepository;
import com.erp.techInovate.techInovate.repository.EmployeeRepository;
import com.erp.techInovate.techInovate.repository.MonthlyAttendanceSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AllowanceTotalService {

    private final AllowanceTotalRepository allowanceTotalRepository;
    private final AllowanceCodeRepository allowanceCodeRepository;
    private final EmployeeRepository employeeRepository;
    private final MonthlyAttendanceSummaryRepository monthlyAttendanceSummaryRepository;

    public AllowanceTotalEntity initializeOrUpdateAllowanceTotal(Long employeeId, LocalDate month) {
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("직원을 찾을 수 없습니다. ID: " + employeeId));

        // 동일한 employeeId와 month 값이 존재하는지 확인
        Optional<AllowanceTotalEntity> existingAllowanceTotal = allowanceTotalRepository.findByEmployeeAndMonth(employee, month.withDayOfMonth(1));

        AllowanceTotalEntity allowanceTotal;
        if (existingAllowanceTotal.isPresent()) {
            // 기존 레코드가 있을 경우 해당 레코드를 가져옴
            allowanceTotal = existingAllowanceTotal.get();
        } else {
            // 기존 레코드가 없을 경우 새 레코드를 생성
            allowanceTotal = new AllowanceTotalEntity();
            allowanceTotal.setEmployee(employee);
            allowanceTotal.setMonth(month.withDayOfMonth(1)); // 월의 첫날로 설정
            double monthlySalary = employee.getSalary() / 12.0;
            double roundedSalary = Math.round(monthlySalary / 1000)*1000;
            allowanceTotal.setBasicSalary(roundedSalary); // 월 기본 급여 만원 단위로 반올림
            allowanceTotal.setTotalAllowance(roundedSalary);
            allowanceTotal.setAllowanceDetails(new HashMap<>()); // 빈 수당 내역 초기화
        }

        allowanceTotalRepository.save(allowanceTotal);
        return allowanceTotal;
    }

    public void addAllowance(Long employeeId, LocalDate month, String allowanceCode, Double fixedAmount, Double percentage, Double dailyRate) {
        // 동일한 employeeId와 month 값의 AllowanceTotalEntity 가져오기 (없으면 새로 생성)
        AllowanceTotalEntity allowanceTotal = initializeOrUpdateAllowanceTotal(employeeId, month);

        AllowanceCodeEntity allowance = allowanceCodeRepository.findByCode(allowanceCode)
                .orElseThrow(() -> new IllegalArgumentException("수당 코드를 찾을 수 없습니다: " + allowanceCode));

        double allowanceAmount = calculateAllowance(allowance, allowanceTotal.getBasicSalary(), allowanceTotal.getEmployee(), allowanceTotal.getMonth(), fixedAmount, percentage, dailyRate);

        // 중복 수당 코드 방지: 이미 해당 수당 코드가 존재하는 경우, 덮어쓰거나 메시지를 띄울 수 있습니다.
        if (allowanceTotal.getAllowanceDetails().containsKey(allowance.getCode())) {
            throw new IllegalArgumentException("해당 수당 코드는 이미 존재합니다: " + allowanceCode);
        }

        // 수당 내역에 추가
        Map<String, Double> allowanceDetails = allowanceTotal.getAllowanceDetails();
        allowanceDetails.put(allowance.getCode(), allowanceAmount);
        allowanceTotal.setAllowanceDetails(allowanceDetails);

        // 총 수당 계산
        allowanceTotal.setTotalAllowance(allowanceTotal.getTotalAllowance() + allowanceAmount);

        allowanceTotalRepository.save(allowanceTotal);
    }

    private double calculateAllowance(AllowanceCodeEntity allowance, double basicSalary, EmployeeEntity employee, LocalDate month, Double fixedAmount, Double percentage, Double dailyRate) {
        switch (allowance.getCalculationType()) {
            case FIXED:
                return fixedAmount != null ? fixedAmount : allowance.getFixedAmount();
            case PERCENTAGE:
                double calculatedPercentage = percentage != null
                        ? basicSalary * percentage / 100.0
                        : basicSalary * allowance.getPercentage() / 100.0;

                // 만 단위로 반올림하여 반환
                return Math.round(calculatedPercentage / 1000) * 1000;
            case DAILY_RATE:
                MonthlyAttendanceSummaryEntity summary = monthlyAttendanceSummaryRepository.findByEmployeeAndMonth(employee, month.withDayOfMonth(1))
                        .orElseThrow(() -> new IllegalArgumentException("해당 월의 근태 요약을 찾을 수 없습니다."));

                // 로그 추가
                System.out.println("Employee ID: " + employee.getEmployeeId());

                System.out.println("Month: " + month);
                System.out.println("Total Work Days: " + summary.getTotalWorkDays());
                return dailyRate != null ? dailyRate * summary.getTotalWorkDays() : allowance.getDailyRate() * summary.getTotalWorkDays();
            default:
                return 0.0;
        }
    }

    public Optional<AllowanceTotalEntity> findById(Long allowanceTotalId) {
        return allowanceTotalRepository.findById(allowanceTotalId);
    }

    public void deleteAllowance(Long employeeId, LocalDate month, String allowanceCode) {
        AllowanceTotalEntity allowanceTotal = initializeOrUpdateAllowanceTotal(employeeId, month);

        // allowanceDetails에서 해당 수당 항목 제거 및 총 수당 업데이트
        Double allowanceAmount = allowanceTotal.getAllowanceDetails().remove(allowanceCode);
        if (allowanceAmount != null) {
            allowanceTotal.setTotalAllowance(allowanceTotal.getTotalAllowance() - allowanceAmount);
            allowanceTotalRepository.save(allowanceTotal);
        } else {
            throw new IllegalArgumentException("삭제할 수당 항목이 없습니다: " + allowanceCode);
        }
    }

    public List<AllowanceTotalEntity> findAllAllowances() {
        return allowanceTotalRepository.findAll();
    }

    public List<AllowanceTotalEntity> searchAllowances(String name, YearMonth month) {
        LocalDate startOfMonth = (month != null) ? month.atDay(1) : null;
        LocalDate endOfMonth = (month != null) ? month.atEndOfMonth() : null;
        return allowanceTotalRepository.searchAllowances(name, startOfMonth, endOfMonth);
    }
}
