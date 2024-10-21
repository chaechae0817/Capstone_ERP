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
    private Long vacationId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "name", nullable = false)
    private LeaveEntity vacationType;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String reason;
}
