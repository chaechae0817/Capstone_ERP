package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.TransferEntity;
import com.erp.techInovate.techInovate.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    public List<TransferEntity> findAll() {
        return transferRepository.findAll();
    }

    public TransferEntity findById(Long id) {
        return transferRepository.findById(id).orElse(null);
    }

    public TransferEntity save(TransferEntity transfer) {
        return transferRepository.save(transfer);
    }

    public void deleteById(Long id) {
        transferRepository.deleteById(id);
    }
}