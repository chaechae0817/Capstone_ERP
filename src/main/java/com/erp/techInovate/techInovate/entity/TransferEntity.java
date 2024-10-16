package com.erp.techInovate.techInovate.entity;

import jakarta.persistence.*;


import java.time.LocalDate;

@Entity
@Table(name = "transfers")
public class TransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferId;

    @Column(nullable = false)
    private Long employeeId;

    @Column(nullable = false)
    private String personnelAppointment; // 부서이동 or 승진

    private String fromDepartment;
    private String toDepartment;
    private String fromPosition;
    private String toPosition;

    @Column(nullable = false)
    private LocalDate transferDate;

    // Getters and Setters
    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getPersonnelAppointment() {
        return personnelAppointment;
    }

    public void setPersonnelAppointment(String personnelAppointment) {
        this.personnelAppointment = personnelAppointment;
    }

    public String getFromDepartment() {
        return fromDepartment;
    }

    public void setFromDepartment(String fromDepartment) {
        this.fromDepartment = fromDepartment;
    }

    public String getToDepartment() {
        return toDepartment;
    }

    public void setToDepartment(String toDepartment) {
        this.toDepartment = toDepartment;
    }

    public String getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(String fromPosition) {
        this.fromPosition = fromPosition;
    }

    public String getToPosition() {
        return toPosition;
    }

    public void setToPosition(String toPosition) {
        this.toPosition = toPosition;
    }

    public LocalDate getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDate transferDate) {
        this.transferDate = transferDate;
    }
}