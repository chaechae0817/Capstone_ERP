package com.erp.techInovate.techInovate.dto;

import com.erp.techInovate.techInovate.entity.DepartmentEntity;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.PositionEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResignationDTO {
    private Long resignationId;
    private EmployeeEntity employee;
    private LocalDate resignationDate; // 퇴사일
    private String resignationReason; // 퇴사 사유
    private PositionEntity lastPosition; // 퇴사 당시 직급
    private DepartmentEntity lastDepartment; // 퇴사 당시 부서
    private Double severancePay; // 퇴직금
    private String notes; // 비고
}
