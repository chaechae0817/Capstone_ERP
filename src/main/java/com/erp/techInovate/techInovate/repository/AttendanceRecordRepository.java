package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.AttendanceRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecordEntity,Long> {
}
