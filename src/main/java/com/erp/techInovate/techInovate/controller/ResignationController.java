package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.ResignationDTO;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.ResignationEntity;
import com.erp.techInovate.techInovate.service.ResignationService;
import com.erp.techInovate.techInovate.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


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
        Optional<ResignationEntity> entity = resignationService.findById(resignationId);
        if(entity.isPresent()){
            ResignationEntity resignation = entity.get();
            EmployeeEntity employee = employeeService.getEmployeeById(resignation.getEmployee().getEmployeeId());
            employee.setStatus("정규");
            employeeService.updateEmployee(employee);
        }
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


    @GetMapping("/resignation/pay")
    public String resignationPay(Model model) {

        // 검색 조건에 따라 퇴사자 목록 필터링
        List<ResignationEntity> resignations = resignationService.findAll();
        List<Map<String, Object>> resignationDetails = new ArrayList<>();

        // 모든 퇴사자 목록을 가져와서 각 퇴사자에 대해 필요한 데이터 추가
        for (ResignationEntity resignation : resignations) {
            Map<String, Object> resignationData = new LinkedHashMap<>();
            EmployeeEntity employee = resignation.getEmployee();

            // 기본 정보 추가
            resignationData.put("employeeName", employee.getName());
            resignationData.put("employeeHireDate", employee.getHireDate());
            resignationData.put("resignationDate", resignation.getResignationDate());

            // 3개월 급여 평균 계산
            double threeMonthAverageSalary = resignationService.getThreeMonthAverageSalary(employee, resignation.getResignationDate());
            System.out.println("3개월 평균 급여: " + threeMonthAverageSalary);

            // 근무 기간 계산 (일 단위)
            long workedDays = java.time.temporal.ChronoUnit.DAYS.between(employee.getHireDate(), resignation.getResignationDate());
            System.out.println("근무 일수: " + workedDays);

            // 퇴직금 계산: (근무 일수 / 365) * 3개월 평균 급여
            double severancePay = (workedDays / 365.0) * threeMonthAverageSalary;
            severancePay = Math.floor(severancePay / 1000) * 1000; // 천 원 단위 절삭
            System.out.println("계산된 퇴직금: " + severancePay);

            // 퇴직금 설정 및 저장
            resignation.setSeverancePay(severancePay);
            resignationService.save(resignation);

            // 데이터 저장
            resignationData.put("threeMonthAverageSalary", threeMonthAverageSalary);
            resignationData.put("severancePay", severancePay);

            resignationDetails.add(resignationData);
        }

        model.addAttribute("resignationDetails", resignationDetails);
        return "resignationPay"; // 해당 HTML 템플릿으로 이동
    }

    @GetMapping("/resignation/search")
    public String searchPayResignation(
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {

        // 퇴사자와 직원 데이터 가져오기
        List<ResignationEntity> resignations = resignationService.findAll();

        // 필터링: startDate와 endDate
        if (startDate != null && endDate != null) {
            resignations = resignations.stream()
                    .filter(r -> !r.getResignationDate().isBefore(startDate) && !r.getResignationDate().isAfter(endDate))
                    .collect(Collectors.toList());
        } else if (startDate != null) { // startDate만 존재할 경우
            resignations = resignations.stream()
                    .filter(r -> !r.getResignationDate().isBefore(startDate)) // startDate 이후 퇴사한 사람
                    .collect(Collectors.toList());
        } else if (endDate != null) { // endDate만 존재할 경우
            resignations = resignations.stream()
                    .filter(r -> !r.getResignationDate().isAfter(endDate)) // endDate 이전에 퇴사한 사람
                    .collect(Collectors.toList());
        }

        // 필터링: 이름 기준
        if (employeeName != null && !employeeName.isEmpty()) {
            resignations = resignations.stream()
                    .filter(r -> r.getEmployee().getName().contains(employeeName))
                    .collect(Collectors.toList());
        }

        // 결과 생성
        List<Map<String, Object>> searchResults = new ArrayList<>();
        for (ResignationEntity resignation : resignations) {
            EmployeeEntity employee = resignation.getEmployee();
            if (resignation.getResignationDate().isAfter(employee.getHireDate())) { // 입사일 < 퇴사일
                Map<String, Object> resultData = new LinkedHashMap<>();
                resultData.put("employeeName", employee.getName());
                resultData.put("employeeHireDate", employee.getHireDate());
                resultData.put("resignationDate", resignation.getResignationDate());
                double threeMonthAverageSalary = resignationService.getThreeMonthAverageSalary(employee, resignation.getResignationDate());
                resultData.put("threeMonthAverageSalary", threeMonthAverageSalary);
                resultData.put("severancePay", resignation.getSeverancePay());
                searchResults.add(resultData);
            }
        }

        model.addAttribute("resignationDetails", searchResults);
        return "resignationPay";
    }
}
