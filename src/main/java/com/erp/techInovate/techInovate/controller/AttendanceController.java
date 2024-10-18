package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.entity.AttendanceEntity;
import com.erp.techInovate.techInovate.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @GetMapping("/list")
    public String list(Model model) {
        List<AttendanceEntity> attendances = attendanceService.findAll();
        model.addAttribute("items", attendances);
        return "attendance/attendanceList"; // 항목 목록 페이지
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<Void> add(@RequestBody AttendanceEntity entity) {
        attendanceService.save(entity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id) {
        attendanceService.delete(id);
        return "redirect:/attendance/attendanceList";
    }

    @GetMapping("/codes")
    @ResponseBody
    public List<AttendanceEntity> getAttendanceCodes() {
        return attendanceService.findAll(); // 모든 근태 코드를 반환
    }
}
