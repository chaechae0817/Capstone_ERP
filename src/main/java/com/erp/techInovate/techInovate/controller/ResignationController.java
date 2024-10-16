package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.ResignationDTO;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.ResignationEntity;
import com.erp.techInovate.techInovate.service.ResignationService;
import com.erp.techInovate.techInovate.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class ResignationController {
    @Autowired
    private ResignationService resignationService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/resignation")
    public String resignationForm(Model model, @SessionAttribute("employeeId") Long employeeId) {
        if(employeeId == null){
            return "redirect:/login";
        }
        EmployeeEntity employee = employeeService.getEmployeeById(employeeId);
        model.addAttribute("employee", employee); // 가져온 EmployeeEntity를 모델에 추가
        return "resignationForm"; // 퇴사 신청 폼으로 이동
    }

    @PostMapping("/resignation/submit")
    public String submitResignation(@RequestParam Long employeeId, @RequestParam String resignationReason) {
        EmployeeEntity employee = employeeService.getEmployeeById(employeeId);
        if (employee == null) {
            // 직원이 존재하지 않을 경우 에러 처리
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        // 퇴사 신청 엔티티 생성
        ResignationEntity resignation = new ResignationEntity();
        resignation.setEmployee(employee); // EmployeeEntity 설정
        resignation.setResignationDate(LocalDate.now()); // 퇴사일 설정
        resignation.setResignationReason(resignationReason);
        resignation.setLastPosition(employee.getPosition()); // 현재 직급을 lastPosition으로 설정
        resignation.setLastDepartment(employee.getDepartment()); //현재 부서를 lastDepartment로 설정

        resignationService.save(resignation); // 퇴사 신청 저장

        return "redirect:/employee/list"; // 홈으로 리다이렉트
    }
    @PostMapping("/resignation/approve/{resignationId}")
    public String approveResignation(@PathVariable Long resignationId, @RequestParam String notes,@SessionAttribute("employeeId") Long employeeId) {
        resignationService.approveResignation(resignationId, notes); // 승인 처리
        EmployeeEntity employee = employeeService.getEmployeeById(employeeId);
        employee.setStatus("퇴사");
        employeeService.updateEmployee(employee);
        return "redirect:/manage/resignations"; // 승인 후 퇴사자 조회 메뉴로 리다이렉트
    }

    @PostMapping("/resignation/reject/{resignationId}")
    public String rejectResignation(@PathVariable Long resignationId) {
        resignationService.rejectResignation(resignationId); // 거부 처리
        return "redirect:/manage/resignations"; // 거부 후 퇴사자 조회 메뉴로 리다이렉트
    }


    @GetMapping("/manage/resignations")
    public String manageResignations(Model model) {
        List<ResignationEntity> resignations = resignationService.findAll(); // 모든 퇴사자 목록 조회
        model.addAttribute("resignations", resignations); // 모델에 추가
        return "resignedEmployees"; //퇴사자 명단
    }

    //퇴사자 정보 상세보기
    @GetMapping("/resignation/manage")
    public String manageResignation(@RequestParam Long resignationId, Model model) {
        ResignationEntity resignation = resignationService.findById(resignationId)
                .orElseThrow(() -> new RuntimeException("Resignation not found")); // 오류 처리
        model.addAttribute("resignation", resignation); // 모델에 추가
        return "manageResignations"; // 퇴사 신청 관리 템플릿으로 이동
    }

}