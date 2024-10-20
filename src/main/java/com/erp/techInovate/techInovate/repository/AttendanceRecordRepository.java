package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.AttendanceRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
}

