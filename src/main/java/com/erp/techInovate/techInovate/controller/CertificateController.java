package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.CareerCertificateDTO;
import com.erp.techInovate.techInovate.service.CertificateService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;


    @GetMapping
    public String getCareerCertificate(HttpSession session, Model model) {
        Long employeeId = (Long) session.getAttribute("employee_id");
        if (employeeId == null) {
            return "redirect:/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

        CareerCertificateDTO careerCertificate = CertificateService.getCareerCertificate(employeeId);
        model.addAttribute("careerCertificate", careerCertificate);
        return "career_certificate"; // 경력 증명서 페이지
    }
}
