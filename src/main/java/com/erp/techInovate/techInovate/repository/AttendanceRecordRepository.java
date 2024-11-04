package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.AttendanceEntity;
import com.erp.techInovate.techInovate.entity.AttendanceRecordEntity;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecordEntity, Long> {

    @Query("SELECT a FROM AttendanceRecordEntity a " +
            "WHERE (:employeeName IS NULL OR a.employee.name LIKE %:employeeName%) " +
            "AND (:startDate IS NULL OR :endDate IS NULL OR a.date BETWEEN :startDate AND :endDate) " +
            "AND (:attendanceId IS NULL OR a.attendance.id = :attendanceId)")
    List<AttendanceRecordEntity> findByCriteria(
            @Param("employeeName") String employeeName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("attendanceId") Long attendanceId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END " +
            "FROM AttendanceRecordEntity a " +
            "WHERE a.employee = :employee " +
            "AND a.date = :date " +
            "AND ((:checkInTime BETWEEN a.checkInTime AND a.checkOutTime) " +
            "OR (:checkOutTime BETWEEN a.checkInTime AND a.checkOutTime) " +
            "OR (a.checkInTime BETWEEN :checkInTime AND :checkOutTime) " +
            "OR (a.checkOutTime BETWEEN :checkInTime AND :checkOutTime))")
    boolean existsByEmployeeAndDateAndOverlappingTime(
            @Param("employee") EmployeeEntity employee,
            @Param("date") LocalDate date,
            @Param("checkInTime") LocalTime checkInTime,
            @Param("checkOutTime") LocalTime checkOutTime
    );

    List<AttendanceRecordEntity> findByEmployeeAndDate(EmployeeEntity employee, LocalDate date);

    @Query("SELECT COALESCE(SUM(a.totalWorkHours), 0) " +
            "FROM AttendanceRecordEntity a " +
            "WHERE a.employee.employeeId = :employeeId " +
            "AND a.date BETWEEN :startOfMonth AND :endOfMonth")
    Optional<Double> findTotalPaidHoursByEmployeeAndMonth(
            @Param("employeeId") Long employeeId,
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth);

}

