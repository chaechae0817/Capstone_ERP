package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.MonthlyDeductionDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthlyDeductionDetailRepository extends JpaRepository<MonthlyDeductionDetailEntity,Long> {
    List<MonthlyDeductionDetailEntity> findByMonthlyDeductionSummaryId(Long summaryId);

}
