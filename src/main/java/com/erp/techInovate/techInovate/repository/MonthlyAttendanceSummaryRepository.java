package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.MonthlyAttendanceSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyAttendanceSummaryRepository extends JpaRepository<MonthlyAttendanceSummaryEntity, Long> {
    // 특정 직원과 월에 대한 요약 데이터 조회
    Optional<MonthlyAttendanceSummaryEntity> findByEmployeeAndMonth(EmployeeEntity employee, LocalDate month);
    List<MonthlyAttendanceSummaryEntity> findByMonth(LocalDate monthStart);




}
