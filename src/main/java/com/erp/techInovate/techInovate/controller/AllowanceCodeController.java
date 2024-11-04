package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.entity.AllowanceCodeEntity;
import com.erp.techInovate.techInovate.service.AllowanceCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/allowanceCode")
public class AllowanceCodeController {
    private final AllowanceCodeService allowanceCodeService;

    @GetMapping("/list")
    public String list(Model model) {
        List<AllowanceCodeEntity> allowanceCodes = allowanceCodeService.findAll();
        model.addAttribute("allowanceCodes", allowanceCodes);
        return "allowance/allowanceCodeList"; // allowance 코드 목록 HTML
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Void> addAllowanceCode(@RequestBody AllowanceCodeEntity allowanceCode) {
        allowanceCodeService.save(allowanceCode);
        return ResponseEntity.ok().build();
    }
}
