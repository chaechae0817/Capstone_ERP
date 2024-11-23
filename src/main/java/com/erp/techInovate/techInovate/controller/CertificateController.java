package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.CareerCertificateDTO;
import com.erp.techInovate.techInovate.dto.EmployeeCertificateDTO;
import com.erp.techInovate.techInovate.entity.*;
import com.erp.techInovate.techInovate.repository.AllowanceCodeRepository;
import com.erp.techInovate.techInovate.repository.DeductionCodeRepository;
import com.erp.techInovate.techInovate.repository.EmployeeRepository;
import com.erp.techInovate.techInovate.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/certificate")
public class CertificateController {

    private final CertificateService certificateService;

    private final CertificateIssueService certificateIssueService;
    private final SalaryDetailService salaryDetailService;
    private final SalaryCalculationService salaryCalculationService;
    private final CompanyService companyService;
    private final EmployeeRepository employeeRepository;


    //경력증명서
    @GetMapping("/career")
    public String getCareerCertificate(HttpSession session, Model model) {
        Long employeeId = (Long) session.getAttribute("employeeId");
        System.out.println("EmployeeId"+employeeId);
        if (employeeId == null) {
            return "redirect:/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
        CareerCertificateDTO careerCertificate = certificateService.getCareerCertificate(employeeId);
        model.addAttribute("careerCertificate", careerCertificate);
        return "career_certificate"; // 경력 증명서 페이지
    }


    //재직 증명서
    @GetMapping("/employee")
    public String getEmployeeCertificate(HttpSession session,Model model,@RequestParam(required = false) String purpose){
        Long employeeId = (Long) session.getAttribute("employeeId");
        if (employeeId == null) {
            return "redirect:/login";
        }
        EmployeeCertificateDTO employeeCertificateDTO = certificateService.getEmployeeCertificate(employeeId,purpose);
        model.addAttribute("employeeCertificate",employeeCertificateDTO);
        return "employee_certificate"; //재직 증명서 페이지

    }

    @GetMapping("/select_purpose")
    public String selectPurpose(
            HttpSession session,
            @RequestParam String purpose,
            Model model) {
        Long employeeId = (Long) session.getAttribute("employeeId");
        if (employeeId == null) {
            return "redirect:/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

        // 용도에 따른 직원 증명서 DTO 생성
        EmployeeCertificateDTO employeeCertificate = certificateService.getEmployeeCertificate(employeeId, purpose);
        model.addAttribute("employeeCertificate", employeeCertificate);

        return "employee_certificate"; // 재직 증명서 페이지로 이동
    }

    @GetMapping("/list")
    public String getCertificateIssue(Model model) {
        List<CertificateIssueEntity> certificateIssues = certificateIssueService.getAllCertificateIssues();
        model.addAttribute("certificateIssues", certificateIssues);
        return "CertificateIssueList"; // CertificateIssueList.html 페이지로 이동
    }

    // 발급 내역 저장 API
    @PostMapping("/issue")
    public ResponseEntity<?> saveCertificateIssue(@RequestBody Map<String, Object> requestData, @SessionAttribute("employeeId") Long employeeId) {
        if (employeeId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        String certificateType = (String) requestData.get("certificateType");
        certificateIssueService.recordIssue(employeeId, certificateType);
        return ResponseEntity.ok().body("발급 내역이 저장되었습니다.");
    }


    @GetMapping("/salary")
    public String generatePayrollCertificate(
            HttpSession session,
            @RequestParam(value = "month", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            Model model) {

        if (month == null) {
            month = YearMonth.now(); // 기본값: 현재 월
        }


        Long employeeId = (Long) session.getAttribute("employeeId");
        if (employeeId == null) {
            return "redirect:/login";
        }
        // 직원 정보 가져오기
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("직원을 찾을 수 없습니다."));

        //직원의 급여 정보를 가져옴
        Map<String, Object> employeeSalaryDetails = salaryDetailService.getMonthlySalaryDetailsForEmployees(employeeId,month);

        // 데이터 유무에 따라 처리
        boolean noData = employeeSalaryDetails == null || employeeSalaryDetails.isEmpty();
        model.addAttribute("noData", noData);

        if (!noData) {
            model.addAttribute("salaryDetails", employeeSalaryDetails);
        }

        // 수당 항목과 공제 항목 헤더 리스트 가져오기
        Set<String> allowanceHeaders = salaryDetailService.getAllowanceHeadersForEmployee(employee,month);
        Set<String> deductionHeaders = salaryDetailService.getDeductionHeaders();
        Optional<CompanyEntity> companyOpt = companyService.getCompany();

        if(companyOpt.isPresent()){
            CompanyEntity company = companyOpt.get();
            model.addAttribute("company",company);
        }
        // 모델에 데이터 추가
        model.addAttribute("employee",employee);
        model.addAttribute("allowanceHeaders", allowanceHeaders);
        model.addAttribute("deductionHeaders", deductionHeaders);
        model.addAttribute("month", month);

        return "salary/payrollCertificate";
    }

}
