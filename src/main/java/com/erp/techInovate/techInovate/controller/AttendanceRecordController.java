package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.entity.AttendanceEntity;
import com.erp.techInovate.techInovate.entity.AttendanceRecordEntity;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.service.AttendanceRecordService;
import com.erp.techInovate.techInovate.service.AttendanceService;
import com.erp.techInovate.techInovate.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/record")
public class AttendanceRecordController {
    @Autowired
    private AttendanceRecordService attendanceRecordService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/list")
    public String list(Model model) {
        List<AttendanceRecordEntity> attendanceRecords = attendanceRecordService.findAll();
        model.addAttribute("attendanceRecords", attendanceRecords);
        // 근태 코드 목록 추가
        List<AttendanceEntity> attendanceCodes = attendanceService.findAll();
        model.addAttribute("attendanceCodes", attendanceCodes);
        return "attendance/attendanceRecordList"; // 항목 목록 페이지
    }

    @GetMapping("/register")
    public String register(Model model) {
        List<EmployeeEntity> employees = employeeService.getAllEmployees(); // 직원 목록 가져오기
        model.addAttribute("employees", employees); // 모델에 추가
        return "attendance/AttendanceRecordForm"; // 등록 페이지
    }
    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<Void> add(@RequestBody AttendanceRecordEntity entity) {
        attendanceRecordService.save(entity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id) {
        attendanceRecordService.delete(id);
        return "redirect:/attendance/attendanceRecordlist"; // 삭제 후 목록으로 리디렉션
    }

    @GetMapping("/search")
    public String searchAttendanceRecords(
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) Long attendance,
            Model model) {

        // 기간 검색 처리
        List<AttendanceRecordEntity> attendanceRecords = attendanceRecordService.searchRecords(employeeName, startDate, endDate, attendance);
        model.addAttribute("attendanceRecords", attendanceRecords);

        // 근태 코드 목록 추가
        List<AttendanceEntity> attendanceCodes = attendanceService.findAll();
        model.addAttribute("attendanceCodes", attendanceCodes);

        // 검색 조건 유지
        model.addAttribute("employeeName", employeeName);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("attendance", attendance);

        return "attendance/attendanceRecordList";
    }


}
