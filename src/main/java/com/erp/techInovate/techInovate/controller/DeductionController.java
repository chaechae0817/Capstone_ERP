package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.entity.MonthlyDeductionDetailEntity;
import com.erp.techInovate.techInovate.entity.MonthlyDeductionSummaryEntity;
import com.erp.techInovate.techInovate.service.SalaryCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/deductionTotal")
public class DeductionController {
    private final SalaryCalculationService salaryCalculationService;

    @GetMapping("/monthlyAll")
    public String getMonthlyDeductionsForAllEmployees(
            @RequestParam(value = "month", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            Model model) {

        if (month == null){
            month = YearMonth.now();
        }

        // 공제 내역이 없는 직원들만 계산 및 저장
        salaryCalculationService.calculateAndSaveDeductionsForAllEmployees(month);

        // 모든 직원의 월별 공제 내역을 가져옴
        List<MonthlyDeductionSummaryEntity> deductionSummaries = salaryCalculationService.getMonthlyDeductionsForAllEmployees(month);

        // 공제 항목 이름 목록 (테이블 헤더로 사용)
        Set<String> deductionHeaders = deductionSummaries.stream()
                .flatMap(summary -> summary.getDeductionDetails().stream())
                .map(MonthlyDeductionDetailEntity::getDeductionName)
                .collect(Collectors.toCollection(LinkedHashSet::new)); // 순서를 유지하여 중복 제거

        // 직원별 공제 내역 변환
        List<Map<String, Object>> deductions = deductionSummaries.stream().map(summary -> {
            Map<String, Object> deductionMap = new LinkedHashMap<>();
            deductionMap.put("employeeName", summary.getEmployee().getName());

            // 공제 항목별 금액 추가
            Map<String, Double> deductionDetails = summary.getDeductionDetails().stream()
                    .collect(Collectors.toMap(
                            MonthlyDeductionDetailEntity::getDeductionName,
                            MonthlyDeductionDetailEntity::getDeductionAmount,
                            (existing, replacement) -> existing, LinkedHashMap::new));

            // 공제 항목에 따라 금액 추가, 없는 항목은 0으로 설정
            for (String header : deductionHeaders) {
                deductionMap.put(header, deductionDetails.getOrDefault(header, 0.0));
            }

            deductionMap.put("totalDeductions", summary.getTotalDeductions());

            System.out.println("deductionMap: "  + deductionMap);

            return deductionMap;
        }).collect(Collectors.toList());

        // 로그로 deductionHeaders와 deductions 리스트 출력
        System.out.println("Deduction Headers: " + deductionHeaders);
        System.out.println("Deductions: " + deductions);

        model.addAttribute("deductionHeaders", deductionHeaders); // 공제 항목 리스트
        model.addAttribute("deductions", deductions); // 직원별 공제 내역 리스트
        model.addAttribute("month", month); // 선택된 월

        return "deduction/monthlyDeductions";
    }
}

