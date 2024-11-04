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
    public Long calculateAnnualIncomeTax(Long annualIncome) {
        TaxBracketEntity bracket = taxBracketRepository.findTaxBracketByIncome(annualIncome);

        if (bracket == null) {
            throw new IllegalArgumentException("과세 구간을 찾을 수 없습니다.");
        }

        Long excessIncome = annualIncome - bracket.getMinIncome();
        Long annualTax = bracket.getFixedAmount() + Math.round(excessIncome * (bracket.getRate() / 100.0));

        return annualTax;
    }

    // 월 소득 기준 소득세 계산
    public Long calculateMonthlyIncomeTax(Long monthlyIncome) {
        Long annualIncome = monthlyIncome * 12;
        Long annualTax = calculateAnnualIncomeTax(annualIncome);
        return Math.round(annualTax / 12.0);
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