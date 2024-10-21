package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.VacationDTO;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.LeaveEntity;
import com.erp.techInovate.techInovate.entity.VacationEntity;
import com.erp.techInovate.techInovate.service.EmployeeService;
import com.erp.techInovate.techInovate.service.LeaveService;
import com.erp.techInovate.techInovate.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/vacation")
public class VacationController {

    @Autowired
    private VacationService vacationService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private EmployeeService employeeService;

    // 휴가 신청 폼을 보여주는 GET 매핑
    @GetMapping("/new")
    public String showVacationForm(Model model) {
        List<EmployeeEntity> employees = employeeService.getAllEmployees(); // 모든 사원 목록을 가져옴
        List<LeaveEntity> leaveTypes = leaveService.getAllLeaveTypes(); // 모든 휴가 종목 목록을 가져옴
        model.addAttribute("employees", employees); // 사원 목록을 모델에 추가
        model.addAttribute("leaveTypes", leaveTypes); // 휴가 종목 목록을 모델에 추가
        model.addAttribute("vacationDTO", new VacationDTO()); // 빈 DTO 폼 데이터를 전달
        return "vacation/new"; // new.html로 폼을 렌더링
    }
    // 휴가 신청 데이터를 처리하는 POST 매핑
    @PostMapping("/new")
    public String applyVacation(@ModelAttribute VacationDTO vacationDTO) {
        vacationService.applyVacation(vacationDTO); // 서비스로 데이터를 넘겨 처리
        return "redirect:/vacation/list"; // 신청 후 리스트 페이지로 리다이렉트
    }
//    @GetMapping("/remaining")
//    public String showRemainingVacations(Model model) {
//        List<EmployeeEntity> employees = employeeService.getAllEmployeesWithRemainingVacation(); // 사원과 남은 휴가 일수 가져오기
//        model.addAttribute("employees", employees); // 남은 휴가 일수를 모델에 추가
//        return "vacation/remaining"; // remainingvacation.html로 렌더링
//    }

    // 신청된 휴가 목록을 보여주는 리스트 페이지
    @GetMapping("/list")
    public String listVacations(Model model) {
        List<VacationEntity> vacations = vacationService.getAllVacations(); // 휴가 목록을 가져옴
        model.addAttribute("vacations", vacations); // 모델에 휴가 목록 전달
        return "vacation/list"; // list.html로 렌더링
    }
}
