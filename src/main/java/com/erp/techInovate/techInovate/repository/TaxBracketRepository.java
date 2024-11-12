package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.TaxBracketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxBracketRepository extends JpaRepository<TaxBracketEntity, Long> {

    @Query("SELECT t FROM TaxBracketEntity t WHERE :income >= t.minIncome AND (:income < t.maxIncome OR t.maxIncome IS NULL)")
    TaxBracketEntity findTaxBracketByIncome(@Param("income") Long income);

    // 연봉이 특정 구간에 포함된 TaxBracketEntity를 조회
    @Query("SELECT t FROM TaxBracketEntity t WHERE :annualSalary BETWEEN t.minIncome AND t.maxIncome")
    Optional<TaxBracketEntity> findBracketByIncome(@Param("annualSalary") double annualSalary);
}
