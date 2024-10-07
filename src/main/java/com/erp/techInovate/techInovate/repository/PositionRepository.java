package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity,Long> {
}
