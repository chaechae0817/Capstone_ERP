package com.erp.techInovate.techInovate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Attendance")
public class AttendanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 항목 ID
    private String name; // 항목 코드 (예: 결근, 조퇴 등)
    private String description; // 항목 설명
}
