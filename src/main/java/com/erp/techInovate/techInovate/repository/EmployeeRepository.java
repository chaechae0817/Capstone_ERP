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


    // 직원 이름으로 검색
    List<EmployeeEntity> findByNameContaining(String name);

    // 직급으로 검색
    List<EmployeeEntity> findByPositionName(String positionName);

    // 부서로 검색
    List<EmployeeEntity> findByDepartmentName(String departmentName);

    // 이름, 직급, 부서 조합으로 검색
    List<EmployeeEntity> findByNameContainingAndPositionNameAndDepartmentName(
            String name, String positionName, String departmentName);

    // 이름과 직급으로 검색
    List<EmployeeEntity> findByNameContainingAndPositionName(String name, String positionName);

    // 이름과 부서로 검색
    List<EmployeeEntity> findByNameContainingAndDepartmentName(String name, String departmentName);

    // 직급과 부서로 검색
    List<EmployeeEntity> findByPositionNameAndDepartmentName(String positionName, String departmentName);
}
