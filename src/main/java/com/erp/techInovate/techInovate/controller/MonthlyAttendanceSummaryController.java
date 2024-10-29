package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.entity.MonthlyAttendanceSummaryEntity;
import com.erp.techInovate.techInovate.service.EmployeeService;
import com.erp.techInovate.techInovate.service.MonthlyAttendanceSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/monthly")
public class MonthlyAttendanceSummaryController {
    private final MonthlyAttendanceSummaryService monthlyAttendanceSummaryService;

    @GetMapping("/list")
    public String list(@RequestParam(value = "month", required = false) String month, Model model) {
        YearMonth selectedMonth = (month != null) ? YearMonth.parse(month) : YearMonth.now();
        List<MonthlyAttendanceSummaryEntity> monthlySummaries = monthlyAttendanceSummaryService.findByMonth(selectedMonth);

        model.addAttribute("monthlySummaries", monthlySummaries);
        model.addAttribute("formattedMonth", selectedMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")));
        return "attendance/monthlySummaryList";
    }

}
