package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.MonthlyDeductionSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MonthlyDeductionSummaryRepository extends JpaRepository<MonthlyDeductionSummaryEntity,Long> {

    List<MonthlyDeductionSummaryEntity> findByMonth(LocalDate month);

    void deleteByEmployeeAndMonth(EmployeeEntity employee, LocalDate month);

    boolean existsByEmployeeAndMonth(EmployeeEntity employee, LocalDate month);

    Optional<MonthlyDeductionSummaryEntity> findByEmployeeAndMonth(EmployeeEntity employee, LocalDate month);



}
