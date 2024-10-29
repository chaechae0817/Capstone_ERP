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
    private Boolean isPaid;
    private Double multiplier = 1.0; //급여 계산 시 사용할 배수 단위 ( 야간 1.5 / 기본 1.0 )

    @Enumerated(EnumType.STRING)
    private AttendanceType type; // 근태 유형 (정상, 특수 근무, 결근 등)

}

