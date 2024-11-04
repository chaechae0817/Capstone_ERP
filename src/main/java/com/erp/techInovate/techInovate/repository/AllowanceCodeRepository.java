package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.AllowanceCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllowanceCodeRepository extends JpaRepository<AllowanceCodeEntity, Long> {
    Optional<AllowanceCodeEntity> findByCode(String code);
}
