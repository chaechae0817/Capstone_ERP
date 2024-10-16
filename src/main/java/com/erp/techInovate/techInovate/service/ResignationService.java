package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.ResignationEntity;
import com.erp.techInovate.techInovate.repository.ResignationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResignationService {
    @Autowired
    private final ResignationRepository resignationRepository;

    public void save(ResignationEntity resignation) {
        resignationRepository.save(resignation);
    }

    public void approveResignation(Long id, String notes) {
        ResignationEntity resignation = resignationRepository.findById(id).orElseThrow();
        resignation.setNotes(notes);
        resignationRepository.save(resignation); // 승인 처리
    }

    public void rejectResignation(Long id) {
        resignationRepository.deleteById(id); // 거부 처리
    }

    public Optional<ResignationEntity> findById(Long id){
        return resignationRepository.findByResignationId(id);
    }

    public List<ResignationEntity> findAll() {
        return resignationRepository.findAll(); // 모든 퇴사자 목록 조회
    }
}

