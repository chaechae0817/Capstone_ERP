package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.EmployeeDTO;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.service.DepartmentService;
import com.erp.techInovate.techInovate.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.erp.techInovate.techInovate.service.EmployeeService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    private final PositionService positionService;

    private final DepartmentService departmentService;


    //직원 목록으로 이동
    @GetMapping("/list")
    public String getEmployees(Model model){
        model.addAttribute("employees",employeeService.getAllEmployees());
        return "employeeList";
    }


    //직원 등록 페이지로 이동
    @GetMapping("/new") // 직원 등록 폼에 접근하기 위한 메서드
    public String createEmployeeForm(Model model) {
        model.addAttribute("employee", new EmployeeDTO());

        // 포지션과 부서 정보를 모델에 추가
        model.addAttribute("positions", positionService.getAllPositions());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "employeeForm"; // employeeForm.html로 이동
    }



    //직원 등록 submit 버튼을 통해 save
    @PostMapping("/save")
    public String createEmployee(@ModelAttribute EmployeeDTO employee, @RequestPart("photo") MultipartFile photo) throws Exception {
        String filename = employeeService.write(photo);
        employeeService.saveEmployee(employee,filename); // 직원 정보 저장
        return "redirect:/employee/list"; // 직원 목록으로 리다이렉트
    }


    //직원 목록에서 search 기능 수행
    @GetMapping("/search")
    public String searchEmployees(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String ssn,
                                  @RequestParam(required = false) Long positionId,
                                  @RequestParam(required = false) String status,
                                  @RequestParam(required = false) Long departmentId,
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

        List<EmployeeEntity> employees = employeeService.searchEmployees(name, ssn, positionId, status,
                departmentId, hireDate, contactInfo, email, birthDate, address, experience, accountNumber, bank);

        model.addAttribute("employees", employees);
        return "employeeList"; // employeeList.html로 이동
    }

    // 수정할 사원 정보를 가져오는 메소드
    @GetMapping("/edit/{id}")
    public String editEmployee(@PathVariable Long id, Model model) {
        EmployeeEntity employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "employeeEdit"; // employeeEdit.html로 이동
    }

    // 수정된 사원 정보를 처리하는 메소드
    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute EmployeeEntity employee) {
        employeeService.updateEmployee(employee);
        return "redirect:/hrm"; // 수정 후 직원 목록으로 리다이렉트
    }
}
