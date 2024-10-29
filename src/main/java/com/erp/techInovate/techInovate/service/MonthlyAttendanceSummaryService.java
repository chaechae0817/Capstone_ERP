package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.AttendanceRecordEntity;
import com.erp.techInovate.techInovate.entity.MonthlyAttendanceSummaryEntity;
import com.erp.techInovate.techInovate.repository.AttendanceRecordRepository;
import com.erp.techInovate.techInovate.repository.MonthlyAttendanceSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonthlyAttendanceSummaryService {
    private final MonthlyAttendanceSummaryRepository monthlyAttendanceSummaryRepository;

    public List<MonthlyAttendanceSummaryEntity> findAll() {
        return monthlyAttendanceSummaryRepository.findAll();
    }
    public List<MonthlyAttendanceSummaryEntity> findByMonth(YearMonth month) {
        return monthlyAttendanceSummaryRepository.findByMonth(month.atDay(1));
    }
}
