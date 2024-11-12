package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.VacationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VacationRepository extends JpaRepository<VacationEntity, Long> {
    Optional<VacationEntity> findByEmployeeEmployeeIdAndLeaveItemId(Long employeeId, Long leaveItemId);
    List<VacationEntity> findByStatus(String status); // 승인된 휴가 목록 조회
}