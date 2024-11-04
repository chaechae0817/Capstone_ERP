package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.DeductionCodeEntity;
import com.erp.techInovate.techInovate.repository.DeductionCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeductionCodeService {
    private final DeductionCodeRepository deductionCodeRepository;

    public List<DeductionCodeEntity> getAllDeductionCodes() {
        return deductionCodeRepository.findAll();
    }

    public DeductionCodeEntity saveDeductionCode(DeductionCodeEntity deductionCode) {
        return deductionCodeRepository.save(deductionCode);
    }

    public void deleteDeductionCode(Long id) {
        deductionCodeRepository.deleteById(id);
    }
}
