package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.CompanyDTO;
import com.erp.techInovate.techInovate.entity.CompanyEntity;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.service.CompanyService;
import com.erp.techInovate.techInovate.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    private EmployeeService employeeService;

    @GetMapping("/list")
    public String listCompany(Model model,@SessionAttribute("employeeId") Long employeeId) {
//        EmployeeEntity employee = employeeService.getEmployeeById(employeeId);
//        if(employee.getDepartment().getName().equals("인사부")){
//
//        }
        Optional<CompanyEntity> company = companyService.getCompany();
        model.addAttribute("company", company.orElse(null));
        return "companyList"; // 회사 목록 페이지 (실제로는 단일 회사 정보)
    }

    @GetMapping("/new")
    public String newCompanyForm(Model model) {
        Optional<CompanyEntity> existingCompany = companyService.getCompany();
        if (existingCompany.isPresent()) {
            return "redirect:/company/list"; // 이미 등록된 경우 목록으로 리다이렉트
        }
        model.addAttribute("company", new CompanyDTO());
        return "companyForm"; // 회사 등록 폼
    }

    @PostMapping("/save")
    public String createCompany(@ModelAttribute CompanyDTO companyDTO) {
        companyService.saveCompany(companyDTO);
        return "redirect:/company/list"; // 회사 목록으로 리다이렉트
    }

    @GetMapping("/{id}/edit")
    public String editCompanyForm(@PathVariable Long id, Model model) {
        CompanyEntity company = companyService.getCompany().orElseThrow(() -> new RuntimeException("Company not found"));
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        companyDTO.setAddress(company.getAddress());
        companyDTO.setContactNumber(company.getContactNumber());
        companyDTO.setEmail(company.getEmail());
        companyDTO.setRegistrationNumber(company.getRegistrationNumber());
        companyDTO.setRepresentativeName(company.getRepresentativeName());
        companyDTO.setBusinessType(company.getBusinessType());
        model.addAttribute("company", companyDTO);
        return "companyForm"; // 회사 수정 폼
    }

    @PostMapping("/{id}")
    public String updateCompany(@PathVariable Long id, @ModelAttribute CompanyDTO companyDTO) {
        companyService.updateCompany(id, companyDTO);
        return "redirect:/company/list"; // 회사 목록으로 리다이렉트
    }

    @PostMapping("/{id}/delete")
    public String deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return "redirect:/company/list"; // 회사 목록으로 리다이렉트
    }
}
