package com.erp.techInovate.techInovate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isRead; // 읽음 여부

    @Column(nullable = false)
    private String type; // 알림 유형 (예: "TRANSFER", "ATTENDANCE")

    @Column
    private String typeId; // 알림에 해당하는 DB 번호


    @Column(nullable = false)
    private String createdAt; // 알림 생성 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false) // 외래키로 employee_id를 추가
    private EmployeeEntity employee;
}
