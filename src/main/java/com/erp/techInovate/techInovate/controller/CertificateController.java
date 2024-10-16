package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.CareerCertificateDTO;
import com.erp.techInovate.techInovate.dto.EmployeeCertificateDTO;
import com.erp.techInovate.techInovate.service.CertificateService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;


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


}
