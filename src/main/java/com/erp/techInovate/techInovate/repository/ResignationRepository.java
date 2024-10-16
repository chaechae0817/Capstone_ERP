package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.ResignationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResignationRepository extends JpaRepository<ResignationEntity, Long> {
    // 직원 ID로 직원 조회
    Optional<ResignationEntity> findByResignationId(Long id);
    List<ResignationEntity> findByEmployee_EmployeeId(Long employeeId);

}