package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.entity.TaxBracketEntity;
import com.erp.techInovate.techInovate.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tax")
public class TaxController {

    private final TaxService taxService;

    // 과세표준 관리 페이지
    @GetMapping("/list")
    public String manageTaxBrackets(Model model) {
        List<TaxBracketEntity> taxBrackets = taxService.getAllTaxBrackets();
        model.addAttribute("taxBrackets", taxBrackets);
        return "deduction/taxList";
    }

    // 과세표준 구간 추가
    @PostMapping("/add")
    @ResponseBody
    public String addTaxBracket(@RequestBody TaxBracketEntity taxBracket) {
        try {
            taxService.saveTaxBracket(taxBracket);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    // 과세표준 구간 삭제
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deleteTaxBracket(@PathVariable Long id) {
        try {
            taxService.deleteTaxBracket(id);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
