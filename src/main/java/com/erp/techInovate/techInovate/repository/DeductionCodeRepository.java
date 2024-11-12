package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.DeductionCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DeductionCodeRepository extends JpaRepository<DeductionCodeEntity, Long> {
    Optional<DeductionCodeEntity> findByCode(String code);
}
