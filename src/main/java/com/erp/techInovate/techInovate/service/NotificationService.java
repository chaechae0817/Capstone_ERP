package com.erp.techInovate.techInovate.service;


import com.erp.techInovate.techInovate.dto.NotificationDTO;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.NotificationEntity;
import com.erp.techInovate.techInovate.repository.NotificationRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    private final EmployeeService employeeService;


    public void addNotification(Long employeeId, String type,Long typeId) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd"); // 날짜 포맷
        EmployeeEntity employee = employeeService.getEmployeeById(employeeId);
        NotificationEntity notification = new NotificationEntity();
        notification.setEmployee(employee);
        notification.setType(type);
        notification.setTypeId(String.valueOf(typeId));
        notification.setCreatedAt(LocalDateTime.now().format(dateFormatter));
        notification.setRead(false); // 기본값은 읽지 않은 상태
        notificationRepository.save(notification);
    }

    public List<NotificationDTO> getUnreadNotifications(Long employeeId) {
        EmployeeEntity employee = employeeService.getEmployeeById(employeeId);
        List<NotificationEntity> notificationEntityList = notificationRepository.findByEmployeeAndIsReadFalse(employee);

        List<NotificationDTO> dtoList = notificationEntityList.stream()
                .map(entity -> {
                    NotificationDTO dto = new NotificationDTO();
                    dto.setId(entity.getId());
                    dto.setType(entity.getType());
                    dto.setEmployeeId(entity.getEmployee().getEmployeeId());
                    dto.setRead(entity.isRead());
                    dto.setCreatedAt(entity.getCreatedAt());
                    dto.setTypeId(entity.getTypeId());
                    return dto;
                }).collect(Collectors.toList());


        return dtoList;
    }

    public void markAsRead(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + notificationId));
        notification.setRead(true); // 읽음 처리
        notificationRepository.save(notification);
    }


}
