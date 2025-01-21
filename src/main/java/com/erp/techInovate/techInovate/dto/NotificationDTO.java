package com.erp.techInovate.techInovate.dto;

import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class NotificationDTO {
    private Long id;
    private boolean isRead; // 읽음 여부

    private String type; // 알림 유형 (예: "TRANSFER", "ATTENDANCE")

    private String typeId; // 알림에 해당하는 DB 번호


    private String createdAt; // 알림 생성 시간

    private Long employeeId; //해당 직원
}
