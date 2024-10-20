package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.AttendanceRecordEntity;
import com.erp.techInovate.techInovate.repository.AttendanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceRecordService {
    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;

    public List<AttendanceRecordEntity> findAll() {
        return attendanceRecordRepository.findAll();
    }

    public void save(AttendanceRecordEntity attendanceRecord) {
        attendanceRecordRepository.save(attendanceRecord);
    }

    public void delete(Long id) {
        attendanceRecordRepository.deleteById(id);
    }

    public List<AttendanceRecordEntity> searchRecords(String employeeName, LocalDate startDate, LocalDate endDate, Long attendanceId) {
        return attendanceRecordRepository.findByCriteria(employeeName, startDate, endDate, attendanceId);
    }


}
