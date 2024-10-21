package com.erp.techInovate.techInovate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "transfers")
public class TransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "from_department_id")
    private DepartmentEntity fromDepartment;

    @ManyToOne
    @JoinColumn(name = "from_position_id")
    private PositionEntity fromPosition;

    @ManyToOne
    @JoinColumn(name = "to_department_id")
    private DepartmentEntity toDepartment;

    @ManyToOne
    @JoinColumn(name = "to_position_id")
    private PositionEntity toPosition;

    private LocalDate transferDate;

    private String personnelAppointment;
}
