package com.erp.techInovate.techInovate.repository;


import com.erp.techInovate.techInovate.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Long> {
    @Query("SELECT t FROM TransferEntity t " +
            "WHERE (:employeeName IS NULL OR t.employee.name LIKE %:employeeName%) " +
            "AND (:toDepartmentId IS NULL OR t.toDepartment.id = :toDepartmentId) " +
            "AND (:toPositionId IS NULL OR t.toPosition.id = :toPositionId) " +
            "AND (:transferDate IS NULL OR t.transferDate = :transferDate) " +
            "AND (:personnelAppointment IS NULL OR t.personnelAppointment LIKE %:personnelAppointment%)")
    List<TransferEntity> searchTransfers(
            @Param("employeeName") String employeeName,
            @Param("toDepartmentId") Long toDepartmentId,
            @Param("toPositionId") Long toPositionId,
            @Param("transferDate") LocalDate transferDate,
            @Param("personnelAppointment") String personnelAppointment);
}
