package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.erp.techInovate.techInovate.service.EmployeeService;

import java.time.LocalDate;
import java.util.List;

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
        return "redirect:/hrm"; // 등록 후 직원 목록으로 리다이렉트
        //redirect -> 저장을 일단 하고 그 뒤에 행동
    }
    @GetMapping("/employee_search")
    public String searchEmployees(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String ssn,
                                  @RequestParam(required = false) String position,
                                  @RequestParam(required = false) String status,
                                  @RequestParam(required = false) String department,
                                  @RequestParam(required = false) String hireDateStr,
                                  @RequestParam(required = false) String contactInfo,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String birthDateStr,
                                  @RequestParam(required = false) String address,
                                  @RequestParam(required = false) String experience,
                                  @RequestParam(required = false) String accountNumber,
                                  @RequestParam(required = false) String bank,
                                  Model model) {
        LocalDate hireDate = hireDateStr != null && !hireDateStr.isEmpty() ? LocalDate.parse(hireDateStr) : null;
        LocalDate birthDate = birthDateStr != null && !birthDateStr.isEmpty() ? LocalDate.parse(birthDateStr) : null;

        List<EmployeeEntity> employees = employeeService.searchEmployees(name, ssn, position, status,
                department, hireDate, contactInfo, email, birthDate, address, experience, accountNumber, bank);

        model.addAttribute("employees", employees);
        return "employeeList"; // employeeList.html로 이동
    }

    // 수정할 사원 정보를 가져오는 메소드
    @GetMapping("/employee/edit/{id}")
    public String editEmployee(@PathVariable Long id, Model model) {
        EmployeeEntity employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "employeeEdit"; // employeeEdit.html로 이동
    }

    // 수정된 사원 정보를 처리하는 메소드
    @PostMapping("/employee/update")
    public String updateEmployee(@ModelAttribute EmployeeEntity employee) {
        employeeService.updateEmployee(employee);
        return "redirect:/hrm"; // 수정 후 직원 목록으로 리다이렉트
    }
}
