package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.AttendanceEntity;
import com.erp.techInovate.techInovate.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<AttendanceEntity> findAll() {
        return attendanceRepository.findAll();
    }

    public AttendanceEntity save(AttendanceEntity entity) {
        return attendanceRepository.save(entity);
    }

    public void delete(Long id) {
        attendanceRepository.deleteById(id);
    }
}