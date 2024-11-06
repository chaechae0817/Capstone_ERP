package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.LeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveEntity,Long> {
    Optional<LeaveEntity> findByName(String name); // 이름으로 LeaveEntity 조회

}
