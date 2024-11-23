package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.SalaryDTO;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.repository.EmployeeRepository;
import com.erp.techInovate.techInovate.service.DepartmentService;
import com.erp.techInovate.techInovate.service.PositionService;
import com.erp.techInovate.techInovate.service.SalaryCalculationService;
import com.erp.techInovate.techInovate.service.SalaryDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/salary")
@RequiredArgsConstructor
public class SalaryDetailController {

    private final SalaryDetailService salaryDetailService;
    private final PositionService positionService;
    private final DepartmentService departmentService;
    private final EmployeeRepository employeeRepository;

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
        model.addAttribute("positions", positionService.getAllPositions());
        model.addAttribute("departments", departmentService.getAllDepartments());

        return "salary/list";
    }

    @GetMapping("/search")
    public String searchSalaryDetails(
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) Long position,
            @RequestParam(required = false) Long department,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            Model model) {

        // 월별 데이터를 먼저 가져옵니다.
        if (month == null) {
            month = YearMonth.now();
        }
        List<Map<String, Object>> allEmployeeSalaryDetails = salaryDetailService.getMonthlySalaryDetailsForAllEmployees(month);

        // 필터링: 직원 이름, 직급, 부서 기준으로 데이터 필터링
        List<Map<String, Object>> filteredSalaryDetails = allEmployeeSalaryDetails.stream()
                .filter(salaryDetail -> {
                    boolean matches = true;
                    if (employeeName != null && !employeeName.isEmpty()) {
                        matches = matches && salaryDetail.get("employeeName").toString().contains(employeeName);
                    }
                    if (position != null) {
                        Object positionId = salaryDetail.get("positionId");
                        matches = matches && salaryDetail.get("positionId").equals(position);
                    }
                    if (department != null) {
                        Object departmentId = salaryDetail.get("departmentId");
                        matches = matches && departmentId != null && departmentId.equals(department);                    }
                    return matches;
                })
                .collect(Collectors.toList());

        // 헤더 정보를 가져옵니다.
        Set<String> allowanceHeaders = salaryDetailService.getAllowanceHeaders();
        Set<String> deductionHeaders = salaryDetailService.getDeductionHeaders();

        // 모델에 필터링된 데이터와 헤더 정보 추가
        model.addAttribute("salaryDetails", filteredSalaryDetails);
        model.addAttribute("allowanceHeaders", allowanceHeaders);
        model.addAttribute("deductionHeaders", deductionHeaders);
        model.addAttribute("month", month);
        model.addAttribute("positions", positionService.getAllPositions());
        model.addAttribute("departments", departmentService.getAllDepartments());

        // 검색 조건 유지
        model.addAttribute("employeeName", employeeName);
        model.addAttribute("positionId", position);
        model.addAttribute("departmentId", department);
        return "salary/list";
    }


    @GetMapping("/android/{employeeId}/{month}")
    @ResponseBody
    public ResponseEntity<SalaryDTO> getSalaryDetails(
            @PathVariable Long employeeId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {

        SalaryDTO salaryDetails = salaryDetailService.getSalaryDetailsForEmployee(employeeId, month);
        return ResponseEntity.ok(salaryDetails);
    }

}
