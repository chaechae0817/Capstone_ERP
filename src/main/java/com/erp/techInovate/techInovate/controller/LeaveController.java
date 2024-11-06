package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.entity.AttendanceEntity;
import com.erp.techInovate.techInovate.entity.LeaveEntity;
import com.erp.techInovate.techInovate.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/leave")
public class LeaveController {
    private final LeaveService leaveService;

    @GetMapping("/list")
    public String list(Model model){
        List<LeaveEntity> leaves = leaveService.findAll();
        model.addAttribute("leaves", leaves);
        return "attendance/leaveList"; // 항목 목록 페이지
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<Void> add(@RequestBody LeaveEntity entity) {
        leaveService.save(entity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id) {
        leaveService.delete(id);
        return "redirect:/leave/list";
    }
}
