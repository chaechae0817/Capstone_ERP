package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.VacationDTO;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.LeaveEntity;
import com.erp.techInovate.techInovate.entity.VacationEntity;
import com.erp.techInovate.techInovate.repository.EmployeeRepository;
import com.erp.techInovate.techInovate.repository.LeaveRepository;
import com.erp.techInovate.techInovate.service.EmployeeService;
import com.erp.techInovate.techInovate.service.LeaveService;
import com.erp.techInovate.techInovate.service.VacationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@Controller
@RequestMapping("/vacation")
public class VacationController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private VacationService vacationService;

    @GetMapping("/new")
    public String showVacationForm(Model model, @SessionAttribute("employeeId") Long employeeId) {
        if (employeeId == null) {
            return "redirect:/login"; // 세션에 employeeId가 없으면 로그인 페이지로 리다이렉트
        }

        // employeeId를 통해 직원 정보를 가져옵니다.
        EmployeeEntity employee = employeeService.getEmployeeById(employeeId);
        VacationDTO vacationDTO = new VacationDTO();

        // vacationDTO에 employeeId를 설정합니다.
        vacationDTO.setEmployeeId(employee.getEmployeeId());
        vacationDTO.setName(employee.getName());
        vacationDTO.setPosition(employee.getPosition().getName());
        vacationDTO.setDepartment(employee.getDepartment().getName());

        List<LeaveEntity> leaveItems = vacationService.getAllLeaveItems();

        model.addAttribute("vacationDTO", vacationDTO);
        model.addAttribute("leaveItems", leaveItems);
        return "vacation/new";
    }

    @PostMapping("/new")
    public String applyForVacation(VacationDTO vacationDTO) {
        vacationService.applyForVacation(vacationDTO);
        return "redirect:/vacation/list"; // 신청 후 리스트 페이지로 리다이렉트
    }
    @GetMapping("/list")
    public String showVacationList(Model model) {
        List<VacationDTO> vacations = vacationService.getAllVacationApplications(); // 모든 신청 내역 가져오기
        model.addAttribute("vacations", vacations);
        return "vacation/list";
    }

    @PostMapping("/approve")
    public String approveVacation(@RequestParam("id") Long id) {
        vacationService.approveVacation(id);
        return "redirect:/vacation/confirmed";
    }

    // 휴가 신청 삭제
    @PostMapping("/delete")
    public String deleteVacation(@RequestParam("id") Long id) {
        vacationService.deleteVacation(id);
        return "redirect:/vacation/list";
    }



    @GetMapping("/confirmed")
    public String showConfirmedVacations(Model model) {
        List<VacationDTO> confirmedVacations = vacationService.getConfirmedVacations();
        model.addAttribute("confirmedVacations", confirmedVacations);
        return "vacation/confirmed";
    }
}