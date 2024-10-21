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

    public PositionEntity findById(Long id) {
        return positionRepository.findById(id).orElse(null);
    }
    public void deletePosition(Long id) {
        positionRepository.deleteById(id);
    }
    // 이름으로 직급 검색
    public PositionEntity findByName(String name) {
        return positionRepository.findByName(name);
    }
}
