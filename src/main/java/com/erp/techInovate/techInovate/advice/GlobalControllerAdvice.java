package com.erp.techInovate.techInovate.advice;


import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute
    public void addUsernameToModel(HttpSession session, Model model){
        // 세션에서 사용자 이름 가져오기
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username); // 모든 모델에 username 추가
        }
    }
}
