package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.dto.SalaryDTO;
import com.erp.techInovate.techInovate.entity.*;
import com.erp.techInovate.techInovate.repository.*;
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
    private final AllowanceCodeRepository allowanceCodeRepository;
    private final MonthlyAttendanceSummaryRepository attendanceSummaryRepository;
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
            salaryDetails.put("departmentId", employee.getDepartment().getId());
            salaryDetails.put("positionId",employee.getPosition().getId());
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

    public Map<String, Object> getMonthlySalaryDetailsForEmployees(Long employeeId,YearMonth month) {
        Map<String, Object> allEmployeeSalaryDetails = new HashMap<String,Object>();
        Map<String, Object> salaryDetails = new LinkedHashMap<>();
        Optional<EmployeeEntity> employeeOpt = employeeRepository.findById(employeeId);
        if(employeeOpt.isPresent()){
            EmployeeEntity employee = employeeOpt.get();
            // 1. 시급과 월 급여 계산
            double hourlyWage = calculateHourlyWage(employee.getSalary());
            double totalPaidHours = getTotalPaidHours(employeeId, month).orElse(0.0);
            double monthlySalary = roundToNearestThousand(totalPaidHours * hourlyWage);
            double totalPaidDays = getMonthlyWorkDays(employee,month);
            Optional<MonthlyAttendanceSummaryEntity> monthlyOpt = attendanceSummaryRepository.findByEmployeeAndMonth(employee,month.atDay(1));
            if(monthlyOpt.isPresent()){
                MonthlyAttendanceSummaryEntity summary = monthlyOpt.get();
                double overTimeHours = summary.getOvertimeHours();
                double holidayWorkHours = summary.getHolidayWorkDays()*8;

                salaryDetails.put("overTimeHours",overTimeHours);
                salaryDetails.put("holidayWorkHours",holidayWorkHours);
            }

            // 2. 추가 수당 정보
            AllowanceTotalEntity allowanceTotal = allowanceTotalRepository.findByEmployeeAndMonth(employee, month.atDay(1)).orElse(null);
            double totalAllowance = allowanceTotal != null ? allowanceTotal.getTotalAllowance() : 0.0;

            // 3. 총 공제 사항
            MonthlyDeductionSummaryEntity deductionSummary = deductionSummaryRepository.findByEmployeeAndMonth(employee, month.atDay(1)).orElse(null);
            double totalDeductions = deductionSummary != null ? deductionSummary.getTotalDeductions() : 0.0;
            Map<String, Double> deductionDetails = deductionSummary != null ? deductionSummary.getDeductionDetails().stream()
                    .collect(Collectors.toMap(
                            MonthlyDeductionDetailEntity::getDeductionName,
                            MonthlyDeductionDetailEntity::getDeductionAmount,
                            (existing, replacement) -> existing, LinkedHashMap::new)) : new LinkedHashMap<>();


            //4.계산 방식 표시
            List<AllowanceCodeEntity> allowances = allowanceCodeRepository.findAll();
            Map<String, Double> allowanceDetails = calculateAllowances(employee, month, monthlySalary, totalPaidDays);

            Map<String, String> allowanceCalculationTypes = new LinkedHashMap<>();
            Map<String, Double> allowanceFixedAmounts = new LinkedHashMap<>();
            Map<String, Double> allowancePercentages = new LinkedHashMap<>();
            Map<String, Double> allowanceDailyRates = new LinkedHashMap<>();

            for (AllowanceCodeEntity allowance : allowances) {
                String name = allowance.getCode();

                switch (allowance.getCalculationType()) {
                    case FIXED:
                        double fixedAmount = allowance.getFixedAmount() != null ? allowance.getFixedAmount() : 0.0;
                        allowanceCalculationTypes.put(name, "FIXED");
                        allowanceFixedAmounts.put(name, fixedAmount);
                        break;

                    case PERCENTAGE:
                        double percentage = allowance.getPercentage() != null ? allowance.getPercentage() : 0.0;
                        allowanceCalculationTypes.put(name, "PERCENTAGE");
                        allowancePercentages.put(name, percentage);
                        break;

                    case DAILY_RATE:
                        double dailyRate = allowance.getDailyRate() != null ? allowance.getDailyRate() : 0.0;
                        allowanceCalculationTypes.put(name, "DAILY_RATE");
                        allowanceDailyRates.put(name, dailyRate);
                        break;

                    default:
                        throw new IllegalArgumentException("알 수 없는 계산 방식: " + allowance.getCalculationType());
                }
            }


            // 데이터 매핑
            salaryDetails.put("employeeName", employee.getName());
            salaryDetails.put("departmentId", employee.getDepartment().getId());
            salaryDetails.put("positionId",employee.getPosition().getId());
            salaryDetails.put("monthlySalary", monthlySalary);
            salaryDetails.put("hourlyWage", hourlyWage);
            salaryDetails.put("totalPaidHours", totalPaidHours);
            salaryDetails.put("totalPaidDays",totalPaidDays);
            salaryDetails.put("totalAllowance", totalAllowance);
            salaryDetails.put("allowanceDetails", allowanceDetails);
            salaryDetails.put("totalDeductions", totalDeductions);
            salaryDetails.put("deductionDetails", deductionDetails);
            salaryDetails.put("allowanceCalculationTypes", allowanceCalculationTypes);
            salaryDetails.put("allowanceFixedAmounts", allowanceFixedAmounts);
            salaryDetails.put("allowancePercentages", allowancePercentages);
            salaryDetails.put("allowanceDailyRates", allowanceDailyRates);

        }

        return salaryDetails;
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

    public Set<String> getAllowanceHeadersForEmployee(EmployeeEntity employee, YearMonth month) {
        return allowanceTotalRepository.findByEmployeeAndMonth(employee, month.atDay(1))
                .map(allowanceTotal -> allowanceTotal.getAllowanceDetails().keySet())
                .orElse(Collections.emptySet()); // AllowanceTotal에 데이터가 없으면 빈 집합 반환
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


    private Map<String, Double> calculateAllowances(EmployeeEntity employee, YearMonth month, double basicSalary, double totalPaidDays) {
        Map<String, Double> allowanceDetails = new LinkedHashMap<>();

        // 전체 수당 코드 가져오기
        List<AllowanceCodeEntity> allowances = allowanceCodeRepository.findAll();

        for (AllowanceCodeEntity allowance : allowances) {
            double allowanceAmount = 0.0;

            switch (allowance.getCalculationType()) {
                case FIXED:
                    allowanceAmount = allowance.getFixedAmount() != null ? allowance.getFixedAmount() : 0.0;
                    break;

                case PERCENTAGE:
                    double percentage = allowance.getPercentage() != null ? allowance.getPercentage() : 0.0;
                    allowanceAmount = basicSalary * (percentage / 100.0);
                    break;

                case DAILY_RATE:
                    double dailyRate = allowance.getDailyRate() != null ? allowance.getDailyRate() : 0.0;
                    allowanceAmount = dailyRate * totalPaidDays;
                    break;

                default:
                    throw new IllegalArgumentException("알 수 없는 계산 방식: " + allowance.getCalculationType());
            }

            allowanceDetails.put(allowance.getCode(), roundToNearestThousand(allowanceAmount));
        }

        return allowanceDetails;
    }


    private double getMonthlyWorkDays(EmployeeEntity employee, YearMonth month) {
        return attendanceSummaryRepository.findByEmployeeAndMonth(employee, month.atDay(1))
                .map(MonthlyAttendanceSummaryEntity::getTotalWorkDays)
                .orElse(0.0); // 근태 요약이 없으면 기본값 0.0 반환
    }

    public SalaryDTO getSalaryDetailsForEmployee(Long employeeId, YearMonth month) {
        Optional<EmployeeEntity> employeeOpt = employeeRepository.findById(employeeId);

        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid employeeId: " + employeeId);
        }

        EmployeeEntity employee = employeeOpt.get();

        // 월 기본급 계산
        double hourlyWage = calculateHourlyWage(employee.getSalary());
        double totalPaidHours = getTotalPaidHours(employeeId, month).orElse(0.0);
        String monthlySalary = String.format("%.2f", totalPaidHours * hourlyWage);

        // 추가 수당 정보
        AllowanceTotalEntity allowanceTotal = allowanceTotalRepository.findByEmployeeAndMonth(employee, month.atDay(1)).orElse(null);
        Map<String, Double> allowanceDetails = allowanceTotal != null ? allowanceTotal.getAllowanceDetails() : Map.of();
        double totalAllowance = allowanceTotal != null ? allowanceTotal.getTotalAllowance() : 0.0;

        // 공제 정보
        MonthlyDeductionSummaryEntity deductionSummary = deductionSummaryRepository.findByEmployeeAndMonth(employee, month.atDay(1)).orElse(null);
        Map<String, Double> deductionDetails = deductionSummary != null ? deductionSummary.getDeductionDetails().stream()
                .collect(Collectors.toMap(
                        MonthlyDeductionDetailEntity::getDeductionName,
                        MonthlyDeductionDetailEntity::getDeductionAmount
                )) : Map.of();
        double totalDeductions = deductionSummary != null ? deductionSummary.getTotalDeductions() : 0.0;

        // DTO 생성
        SalaryDTO salaryDTO = new SalaryDTO();
        salaryDTO.setEmployeeId(employee.getEmployeeId());
        salaryDTO.setEmployeeName(employee.getName());
        salaryDTO.setMonthlySalary(monthlySalary);
        salaryDTO.setAllowanceDetails(allowanceDetails);
        salaryDTO.setTotalAllowance(totalAllowance);
        salaryDTO.setDeductionDetails(deductionDetails);
        salaryDTO.setTotalDeductions(totalDeductions);

        return salaryDTO;
    }

}
