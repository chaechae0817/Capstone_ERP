package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.DepartmentEntity;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long>, JpaSpecificationExecutor<EmployeeEntity> {
    // 직원 상태 업데이트
    @Modifying
    @Query("UPDATE EmployeeEntity e SET e.status = :status WHERE e.employeeId = :employeeId")
    void updateEmployeeStatus(@Param("employeeId") Long employeeId, @Param("status") String status);

    Optional<EmployeeEntity> findByEmployeeNumberAndEmail(String employeeNumber, String email);

}
