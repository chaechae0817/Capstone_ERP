package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.AllowanceCodeEntity;
import com.erp.techInovate.techInovate.repository.AllowanceCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllowanceCodeService {
    private final AllowanceCodeRepository allowanceCodeRepository;

    public List<AllowanceCodeEntity> findAll() {
        return allowanceCodeRepository.findAll();
    }

    public void save(AllowanceCodeEntity allowanceCode) {
        allowanceCodeRepository.save(allowanceCode);
    }
}
