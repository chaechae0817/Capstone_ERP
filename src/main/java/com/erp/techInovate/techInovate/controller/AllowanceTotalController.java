package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.entity.AllowanceTotalEntity;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.service.AllowanceCodeService;
import com.erp.techInovate.techInovate.service.AllowanceTotalService;
import com.erp.techInovate.techInovate.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/allowanceTotal")
public class AllowanceTotalController {

    private final AllowanceTotalService allowanceTotalService;
    private final EmployeeService employeeService;
    private final AllowanceCodeService allowanceCodeService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "allowance/allowanceTotalList";
    }

    @GetMapping("/allList")
    public String listAllAllowanceTotals(Model model) {
        List<AllowanceTotalEntity> allowanceTotals = allowanceTotalService.findAllAllowances();
        model.addAttribute("allowanceTotals", allowanceTotals);
        return "allowance/allowanceTotalAllList"; // allowanceTotalList.html로 전달
    }

    // 이름과 월을 기준으로 검색
    @GetMapping("/search")
    public String searchAllowanceTotals(@RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "month", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
                                        Model model) {
        List<AllowanceTotalEntity> allowanceTotals = allowanceTotalService.searchAllowances(name, month);

        model.addAttribute("allowanceTotals", allowanceTotals);
        model.addAttribute("name", name);
        model.addAttribute("month", month);
        return "allowance/allowanceTotalAllList";
    }




    @GetMapping("/view")
    public String viewAllowance(@RequestParam("employeeId") Long employeeId,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
                                Model model) {
        if (month == null) {
            month = YearMonth.now(); // 기본값은 이번 달
        }

        LocalDate firstDayOfMonth = month.atDay(1); // YearMonth를 LocalDate로 변환
        AllowanceTotalEntity allowanceTotal = allowanceTotalService.initializeOrUpdateAllowanceTotal(employeeId, firstDayOfMonth);

        model.addAttribute("allowanceTotal", allowanceTotal);
        model.addAttribute("allowanceCodes", allowanceCodeService.findAll());
        model.addAttribute("month", month); // YearMonth 형식으로 전달

        return "allowance/allowanceTotalView";
    }

    @PostMapping("/addAllowance")
    public String addAllowance(@RequestParam("employeeId") Long employeeId,
                               @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
                               @RequestParam("allowanceCode") String allowanceCode,
                               @RequestParam(value = "fixedAmount", required = false) Double fixedAmount,
                               @RequestParam(value = "percentage", required = false) Double percentage,
                               @RequestParam(value = "dailyRate", required = false) Double dailyRate,
                               Model model) {
        // yearMonth를 LocalDate로 변환하여 해당 월의 첫날을 기준으로 사용
        LocalDate selectedMonth = month.atDay(1);

        // 수당 추가
        allowanceTotalService.addAllowance(employeeId, selectedMonth, allowanceCode, fixedAmount, percentage, dailyRate);

        // 변경된 allowanceTotal 객체를 다시 조회
        AllowanceTotalEntity allowanceTotal = allowanceTotalService.initializeOrUpdateAllowanceTotal(employeeId, selectedMonth);

        model.addAttribute("allowanceTotal", allowanceTotal);
        model.addAttribute("allowanceCodes", allowanceCodeService.findAll());
        model.addAttribute("month", month); // 선택된 month 값 전달

        return "allowance/allowanceTotalView";
    }


    @DeleteMapping("/deleteAllowance")
    public ResponseEntity<Void> deleteAllowance(@RequestParam("employeeId") Long employeeId,
                                                @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
                                                @RequestParam("allowanceCode") String allowanceCode) {
        try {
            LocalDate firstDayOfMonth = month.atDay(1);
            allowanceTotalService.deleteAllowance(employeeId, firstDayOfMonth, allowanceCode);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
