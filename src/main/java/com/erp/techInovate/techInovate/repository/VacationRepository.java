package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.VacationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacationRepository extends JpaRepository<VacationEntity, Long> {
    List<VacationEntity> findByEmployee_EmployeeId(Long employeeId);
}