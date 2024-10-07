package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity,Long> {
}
