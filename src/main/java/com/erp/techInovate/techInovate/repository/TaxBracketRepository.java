package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.TaxBracketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxBracketRepository extends JpaRepository<TaxBracketEntity, Long> {
    @Query("SELECT s FROM TaxBracketEntity s WHERE :salary BETWEEN s.minSalary AND s.maxSalary AND s.dependentCount = :dependentCount")
    Optional<TaxBracketEntity> findTaxBySalaryAndDependents(@Param("salary") Long salary, @Param("dependentCount") int dependentCount);
}
