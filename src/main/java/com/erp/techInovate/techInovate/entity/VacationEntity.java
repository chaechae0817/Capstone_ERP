package com.erp.techInovate.techInovate.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Entity
@Table(name = "vacations")
public class VacationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employeeId", nullable = false)
    private EmployeeEntity employee;  // 사원 정보 연결

    @ManyToOne
    @JoinColumn(name = "leave_item_id", referencedColumnName = "id", nullable = false)
    private LeaveEntity leaveItem;  // 휴가 항목 정보

    @Column(nullable = false)
    private int remainingDays;

    @Column(nullable = false)
    private Integer days = 0;

    @Column(nullable = false)
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = "PENDING";
        }}
}
