package com.erp.techInovate.techInovate.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "CertificateIssue")
@Setter
@Getter
@ToString
public class CertificateIssueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee; // 증명서를 발급받은 직원
    @Column(nullable = false)
    private LocalDateTime issueDate; // 발급 일자

    @Column(nullable = false)
    private String certificateType; // 증명서 유형 (예: 재직 증명서, 경력 증명서 등)

    // 엔티티를 저장할 때 발급번호를 자동 생성하는 메서드

}
