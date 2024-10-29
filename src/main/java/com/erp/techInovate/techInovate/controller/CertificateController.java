package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.CareerCertificateDTO;
import com.erp.techInovate.techInovate.dto.EmployeeCertificateDTO;
import com.erp.techInovate.techInovate.entity.CertificateIssueEntity;
import com.erp.techInovate.techInovate.service.CertificateIssueService;
import com.erp.techInovate.techInovate.service.CertificateService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CertificateIssueService certificateIssueService;


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
}
