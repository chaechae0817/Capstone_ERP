package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.PositionEntity;
import com.erp.techInovate.techInovate.repository.PositionRepository; // 레포지토리 필요
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    public List<PositionEntity> getAllPositions() {
        return positionRepository.findAll();
    }

    public PositionEntity savePosition(PositionEntity position) {
        return positionRepository.save(position);
    }

    public void deletePosition(Long id) {
        positionRepository.deleteById(id);
    }
}
