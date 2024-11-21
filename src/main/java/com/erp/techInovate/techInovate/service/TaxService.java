package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.TaxBracketEntity;
import com.erp.techInovate.techInovate.repository.TaxBracketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxService {

    private final TaxBracketRepository taxBracketRepository;

    // 연 소득 기준 소득세 계산
    // 월 소득 기준 소득세 계산 (2024 간이세액표)
    public Long calculateSimplifiedMonthlyTax(Long monthlySalary, int dependentCount) {
        TaxBracketEntity tax = taxBracketRepository
                .findTaxBySalaryAndDependents(monthlySalary, dependentCount)
                .orElseThrow(() -> new IllegalArgumentException("해당 급여와 부양가족 수에 해당하는 간이세액을 찾을 수 없습니다."));

        return tax.getSimplifiedTaxAmount();
    }


    // 과세표준 구간 추가
    @Transactional
    public void saveTaxBracket(TaxBracketEntity taxBracket) {
        taxBracketRepository.save(taxBracket);
    }

    // 과세표준 구간 삭제
    @Transactional
    public void deleteTaxBracket(Long id) {
        taxBracketRepository.deleteById(id);
    }

    // 모든 과세표준 구간 조회
    public List<TaxBracketEntity> getAllTaxBrackets() {
        return taxBracketRepository.findAll();
    }
}