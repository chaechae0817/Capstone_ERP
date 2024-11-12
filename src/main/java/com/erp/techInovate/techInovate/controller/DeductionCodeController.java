package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.entity.DeductionCodeEntity;
import com.erp.techInovate.techInovate.service.DeductionCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/deduction")
public class DeductionCodeController {
    private final DeductionCodeService deductionCodeService;

    @GetMapping("/list")
    public String getAllDeductions(Model model) {
        model.addAttribute("deductions", deductionCodeService.getAllDeductionCodes());
        return "/deduction/deductionCodeList";
    }

    @PostMapping("/add")
    @ResponseBody // JSON 응답을 위해 추가
    public String addDeduction(@RequestBody DeductionCodeEntity deductionCode) {
        if (deductionCode.getCode() == null || deductionCode.getDescription() == null || deductionCode.getPercentage() == null) {
            return "모든 필드를 입력해주세요.";
        }
        deductionCodeService.saveDeductionCode(deductionCode);
        return "success";
    }


    @GetMapping("/deductions/delete/{id}")
    public String deleteDeduction(@PathVariable Long id) {
        deductionCodeService.deleteDeductionCode(id);
        return "redirect:/deduction/CodeList";
    }
}
