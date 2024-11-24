package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    private EmployeeService employeeService;


    //로그인 HTML 페이지로 이동
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html로 이동
    }


    //submit 버튼으로 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String employeeNumber, @RequestParam String email, Model model, HttpSession session) {
        Optional<EmployeeEntity> employee = employeeService.login(employeeNumber, email);

        if (employee.isPresent()) {
            // 로그인 성공 시 세션에 employeeId 저장
            session.setAttribute("employeeId", employee.get().getEmployeeId()); // employeeId를 세션에 저장
            session.setAttribute("username", employee.get().getName()); // username(직원 이름)을 세션에 저장
            model.addAttribute("message", "로그인 성공!");
            return "redirect:/employee/list"; // 홈 페이지로 리다이렉트
        } else {
            model.addAttribute("error", "로그인 실패! 사원번호와 이메일을 확인하세요.");
            return "login"; // 다시 로그인 페이지로 이동
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }

    //안드로이드
    @PostMapping("/android/login")
    @ResponseBody
    public Map<String,Object> androidLogin(@RequestParam String employeeNumber, @RequestParam String email) {
        Optional<EmployeeEntity> employee = employeeService.login(employeeNumber, email);
        Map<String, Object> response = new HashMap<>();
        if (employee.isPresent()) {
            // 로그인 성공 시
            response.put("success", true);
            response.put("employee", employee.get());
        } else {
            // 로그인 실패 시
            response.put("success", false);
        }

        return response;
    }
}

