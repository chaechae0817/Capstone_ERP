package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity,Long> {
}
