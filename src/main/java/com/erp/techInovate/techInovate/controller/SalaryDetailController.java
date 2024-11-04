package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.service.SalaryCalculationService;
import com.erp.techInovate.techInovate.service.SalaryDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Controller
@RequestMapping("/salary")
@RequiredArgsConstructor
public class SalaryDetailController {

    private final SalaryDetailService salaryDetailService;

    @GetMapping("/list")
    public String getMonthlySalaryDetailsForAllEmployees(
            @RequestParam(value = "month", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            Model model) {

        if (month == null) {
            month = YearMonth.now();
        }

        // 월별 모든 직원의 급여 정보를 가져옴
        List<Map<String, Object>> allEmployeeSalaryDetails = salaryDetailService.getMonthlySalaryDetailsForAllEmployees(month);

        // 수당 항목과 공제 항목 헤더 리스트 가져오기
        Set<String> allowanceHeaders = salaryDetailService.getAllowanceHeaders();
        Set<String> deductionHeaders = salaryDetailService.getDeductionHeaders();

        // 모델에 데이터 추가
        model.addAttribute("salaryDetails", allEmployeeSalaryDetails);
        model.addAttribute("allowanceHeaders", allowanceHeaders);
        model.addAttribute("deductionHeaders", deductionHeaders);
        model.addAttribute("month", month);

        return "salary/list";
    }
}
