package com.erp.techInovate.techInovate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Position")
public class PositionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키

    private String name; // 직급 이름 (예: 인턴, 직원, 대리, 과장, 차장, 부장)


}
