package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.VacationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VacationRepository extends JpaRepository<VacationEntity, Long> {
    Optional<VacationEntity> findByEmployeeEmployeeIdAndLeaveItemId(Long employeeId, Long leaveItemId);
    List<VacationEntity> findByStatus(String status); // 승인된 휴가 목록 조회
    @Query("SELECT v FROM VacationEntity v " +
            "WHERE v.status = 'APPROVED' " +
            "AND (:employeeName IS NULL OR v.employee.name LIKE %:employeeName%) " +
            "AND (:startDate IS NULL OR v.startDate >= :startDate) " +
            "AND (:endDate IS NULL OR v.endDate <= :endDate) " +
            "AND (:leaveItemName IS NULL OR v.leaveItem.name LIKE %:leaveItemName%)")
    List<VacationEntity> searchConfirmedVacations(
            @Param("employeeName") String employeeName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("leaveItemName") String leaveItemName);
}