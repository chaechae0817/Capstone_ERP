package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.AttendanceRecordEntity;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.MonthlyAttendanceSummaryEntity;
import com.erp.techInovate.techInovate.repository.AttendanceRecordRepository;
import com.erp.techInovate.techInovate.repository.AttendanceRepository;
import com.erp.techInovate.techInovate.repository.MonthlyAttendanceSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceRecordService {
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final MonthlyAttendanceSummaryRepository monthlyAttendanceSummaryRepository;
    private final AttendanceRepository attendanceRepository;


    public List<AttendanceRecordEntity> findAll() {
        return attendanceRecordRepository.findAll();
    }
    public void save(AttendanceRecordEntity attendanceRecord) {
        boolean exists = attendanceRecordRepository.existsByEmployeeAndDateAndOverlappingTime(
                attendanceRecord.getEmployee(),
                attendanceRecord.getDate(),
                attendanceRecord.getCheckInTime(),
                attendanceRecord.getCheckOutTime()
        );
        if (exists) {
            throw new IllegalArgumentException("해당 직원의 같은 날짜 및 시간대에 이미 근태 기록이 존재합니다.");
        }

        boolean alreadyCounted = isAlreadyCounted(attendanceRecord.getEmployee(), attendanceRecord.getDate());

        try {
            attendanceRecordRepository.save(attendanceRecord);
            updateMonthlySummary(attendanceRecord,alreadyCounted);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("해당 직원의 같은 날짜 및 시간대에 이미 근태 기록이 존재합니다.", e);
        }
    }

    public boolean isAlreadyCounted(EmployeeEntity employee, LocalDate recordDate) {
        List<AttendanceRecordEntity> records = attendanceRecordRepository.findByEmployeeAndDate(employee, recordDate);
        System.out.println("Records found: " + records.size()); // 찾은 레코드 개수 확인
        return !records.isEmpty();
    }


    private void updateMonthlySummary(AttendanceRecordEntity attendanceRecord,boolean alreadyCounted) {
        LocalDate recordDate = attendanceRecord.getDate();
        YearMonth month = YearMonth.from(recordDate);
        MonthlyAttendanceSummaryEntity summary = monthlyAttendanceSummaryRepository.findByEmployeeAndMonth(
                attendanceRecord.getEmployee(),
                month.atDay(1)  // Month의 첫 번째 날로 설정
        ).orElseGet(() -> {
            MonthlyAttendanceSummaryEntity newSummary = new MonthlyAttendanceSummaryEntity();
            newSummary.setEmployee(attendanceRecord.getEmployee());
            newSummary.setMonth(month.atDay(1));
            newSummary.setNormalWorkDays(0.0); //정상 근무
            newSummary.setHalfWorkDays(0.0); //조퇴 or 반차
            newSummary.setTotalWorkDays(0.0); //한 달 총 근무일
            newSummary.setTotalAbsenceDays(0.0); //총 결근 일
            newSummary.setTotalWorkHours(0.0); //총 일한 시간
            newSummary.setTotalPaidWorkHours(0.0); //총 일한 시간
            newSummary.setSpecialWorkDays(0.0); //출장,등 특별한 이유가 인정되는 날
            newSummary.setOvertimeHours(0.0); //추가 근무 (야갼)
            newSummary.setHolidayWorkDays(0.0); //공휴일 근무
            return newSummary;
        });

        // 근태 유형에 따른 요약 데이터 업데이트
        double workHours = attendanceRecord.getTotalWorkHours(); //저장한 날의 총 근무 시간을 가져옴.

//        // 이미 기록된 날짜인지 확인
//        boolean alreadyCounted = attendanceRecordRepository.existsByEmployeeAndDate(
//                attendanceRecord.getEmployee(), recordDate);


//        boolean alreadyCounted = isAlreadyCounted(attendanceRecord.getEmployee(),recordDate);
        System.out.println("employee"+attendanceRecord.getEmployee().getEmployeeId()+"Date"+recordDate);
        System.out.println("이미 기록된 날짜? "+alreadyCounted);
        // 요약 데이터 업데이트 -> 다시 천천히..
        switch (attendanceRecord.getAttendance().getType()) {
            case NORMAL: //정상 근무
                if (!alreadyCounted) {
                    if (workHours + 1 >= 8) {
                        summary.setNormalWorkDays(summary.getNormalWorkDays() + 1);
                    } else if (workHours + 0.5 >= 4) {
                        summary.setHalfWorkDays(summary.getHalfWorkDays() + 0.5);
                    }
                }
                summary.setTotalPaidWorkHours(summary.getTotalPaidWorkHours() + workHours);
                break;
            case ABSENCE: //조퇴 , 결근
                if (!alreadyCounted) {
                    summary.setTotalAbsenceDays(summary.getTotalAbsenceDays() + 1);
                }
                summary.setTotalPaidWorkHours(summary.getTotalPaidWorkHours() + workHours);
                break;
            case SPECIAL: //특수 근무
                if (!alreadyCounted) {
                    summary.setSpecialWorkDays(summary.getSpecialWorkDays() + 1);
                }
                summary.setTotalPaidWorkHours(summary.getTotalPaidWorkHours() + workHours);
                break;
            case HOLIDAY: //휴일 근무
                if (!alreadyCounted) {
                    summary.setHolidayWorkDays(summary.getHolidayWorkDays() + 1);
                }
                summary.setTotalPaidWorkHours(summary.getTotalPaidWorkHours() + workHours);
                break;
        }

        // 총 근무 시간과 근무 일수 업데이트
        summary.setTotalWorkHours(summary.getTotalWorkHours() + workHours);
        if (!alreadyCounted) {
            summary.setTotalWorkDays(summary.getNormalWorkDays() + summary.getHalfWorkDays() + summary.getHolidayWorkDays());
        }

        // 야근 시간 계산
        if (attendanceRecord.getCheckOutTime().isAfter(LocalTime.parse("18:00"))) {
            double overtimeHours = LocalTime.parse("18:00").until(attendanceRecord.getCheckOutTime(), java.time.temporal.ChronoUnit.HOURS);
            summary.setOvertimeHours(summary.getOvertimeHours() + Math.max(0, overtimeHours));
        }

        monthlyAttendanceSummaryRepository.save(summary);
    }


    public void delete(Long id) {
        attendanceRecordRepository.deleteById(id);
    }

    public List<AttendanceRecordEntity> searchRecords(String employeeName, LocalDate startDate, LocalDate endDate, Long attendanceId) {
        return attendanceRecordRepository.findByCriteria(employeeName, startDate, endDate, attendanceId);
    }
}
