package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.LeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveEntity,Long> {
}
