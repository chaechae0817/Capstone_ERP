package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.AttendanceRecordDTO;
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
import java.util.Map;

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
        return "redirect:/record/list"; // 삭제 후 목록으로 리디렉션
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


    // 특정 직원의 특정 월 근태 기록 조회
    @GetMapping("/android/list/{employeeId}/{month}")
    @ResponseBody
    public ResponseEntity<List<AttendanceRecordDTO>> getAttendanceRecordsByEmployeeAndMonth(
            @PathVariable Long employeeId,
            @PathVariable int month) {

        List<AttendanceRecordDTO> records = attendanceRecordService.getAttendanceRecordsByEmployeeAndMonth(employeeId, month);
        return ResponseEntity.ok(records);
    }


    @PostMapping("/qr/register")
    @ResponseBody
    public ResponseEntity<String> registerAttendance(@RequestBody Map<String, Long> payload) {
        Long employeeId = payload.get("employeeId");
        if (employeeId == null) {
            return ResponseEntity.badRequest().body("{\"status\":\"error\", \"message\":\"employeeId가 누락되었습니다.\"}");
        }

        try {
            EmployeeEntity employee = employeeService.getEmployeeById(employeeId);
            if (employee == null) {
                return ResponseEntity.badRequest().body("{\"status\":\"error\", \"message\":\"해당 직원이 존재하지 않습니다.\"}");
            }

            AttendanceRecordEntity record = attendanceRecordService.recordAttendance(employeeId);
            String message = (record.getCheckOutTime() == null)
                    ? employee.getName() + "님, 출근 처리 완료되었습니다"
                    : employee.getName() + "님, 퇴근 처리 완료되었습니다.";

            return ResponseEntity.ok("{\"status\":\"success\", \"message\":\"" + message + "\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }


    // QR 스캔 페이지 매핑
    @GetMapping("/qr-scanner")
    public String showQRScannerPage() {
        return "qr/attendanceQRScanner";
    }

}
