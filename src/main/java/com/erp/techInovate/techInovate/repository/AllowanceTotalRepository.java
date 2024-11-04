package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.AllowanceCodeEntity;
import com.erp.techInovate.techInovate.entity.AllowanceTotalEntity;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface AllowanceTotalRepository extends JpaRepository<AllowanceTotalEntity, Long> {
    Optional<AllowanceTotalEntity> findById(Long allowanceTotalId);
    Optional<AllowanceTotalEntity> findByEmployeeAndMonth(EmployeeEntity employee, LocalDate month);

    @Query("SELECT a FROM AllowanceTotalEntity a " +
            "WHERE (:name IS NULL OR a.employee.name LIKE %:name%) " +
            "AND (:startOfMonth IS NULL OR a.month BETWEEN :startOfMonth AND :endOfMonth)")
    List<AllowanceTotalEntity> searchAllowances(
            @Param("name") String name,
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth);

}
