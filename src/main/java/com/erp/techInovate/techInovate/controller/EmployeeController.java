package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.erp.techInovate.techInovate.service.EmployeeService;

@Controller
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/hrm")
    public String getEmployees(Model model){
        model.addAttribute("employees",employeeService.getAllEmployees());
        return "employeeList";
    }
    // 특정 직원의 상세 페이지
    @GetMapping("/employee/{id}")
    public String getEmployeeDetails(@PathVariable("id") Long id, Model model) {
        EmployeeEntity employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "employeeInformation"; // employee-details.html 파일로 이동
    }

    @GetMapping("/employee/new")
    public String createEmployeeForm(Model model) {
        model.addAttribute("employee", new EmployeeEntity());
        return "employeeForm"; // employeeForm.html로 이동
    }

    @PostMapping("/employee")
    public String createEmployee(@ModelAttribute EmployeeEntity employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/list"; // 등록 후 직원 목록으로 리다이렉트
        //redirect -> 저장을 일단 하고 그 뒤에 행동
    }
}
