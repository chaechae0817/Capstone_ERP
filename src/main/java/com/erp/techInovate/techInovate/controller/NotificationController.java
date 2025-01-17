package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.NotificationDTO;
import com.erp.techInovate.techInovate.entity.NotificationEntity;
import com.erp.techInovate.techInovate.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;



    @GetMapping("/unread/{employeeId}")
    @ResponseBody
    public List<NotificationDTO> getUnreadNotifications(@PathVariable Long employeeId) {
        return notificationService.getUnreadNotifications(employeeId);
    }


    @PostMapping("/read/{id}")
    @ResponseBody
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

}
