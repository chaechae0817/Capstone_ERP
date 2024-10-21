package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.AttendanceEntity;
import com.erp.techInovate.techInovate.entity.LeaveEntity;
import com.erp.techInovate.techInovate.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;
    public List<LeaveEntity> findAll() {
        return leaveRepository.findAll();
    }

    public LeaveEntity save(LeaveEntity entity) {
        return leaveRepository.save(entity);
    }

    public void delete(Long id) {
        leaveRepository.deleteById(id);
    }

    public List<LeaveEntity> getAllLeaveTypes() {
        return leaveRepository.findAll();
    }
}
