package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.dto.AttendanceRecordDTO;
import com.erp.techInovate.techInovate.entity.*;
import com.erp.techInovate.techInovate.repository.AttendanceRecordRepository;
import com.erp.techInovate.techInovate.repository.AttendanceRepository;
import com.erp.techInovate.techInovate.repository.MonthlyAttendanceSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceRecordService {
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final MonthlyAttendanceSummaryRepository monthlyAttendanceSummaryRepository;
    private final SalaryCalculationService salaryCalculationService;
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;


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

            // 근태 기록이 변경되었으므로 해당 월의 공제 내역을 업데이트
            YearMonth month = YearMonth.from(attendanceRecord.getDate());
            salaryCalculationService.updateMonthlyDeductions(attendanceRecord.getEmployee().getEmployeeId(), month);

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
        double workHours = attendanceRecord.getTotalWorkHours(); //저장한 날의 총 근무 시간을 가져옴. -> 이미 계산된 값(휴게시간이 제외된 값)
        double multiplier = attendanceRecord.getAttendance().getMultiplier(); //근무 배수
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
                summary.setTotalPaidWorkHours(summary.getTotalPaidWorkHours() + (workHours * multiplier));
                break;
            case ABSENCE: //조퇴 , 결근
                if (!alreadyCounted) {
                    summary.setTotalAbsenceDays(summary.getTotalAbsenceDays() + 1);
                }
                summary.setTotalPaidWorkHours(summary.getTotalPaidWorkHours() + (workHours * multiplier));
                break;
            case SPECIAL: //특수 근무 -> 시간당 1.5배
                summary.setSpecialWorkDays(summary.getSpecialWorkDays() + 1);
                summary.setTotalPaidWorkHours(summary.getTotalPaidWorkHours() + (workHours * multiplier));
                break;
            case HOLIDAY: //휴일 근무
                if (!alreadyCounted) {
                    summary.setHolidayWorkDays(summary.getHolidayWorkDays() + 1);
                }
                summary.setTotalPaidWorkHours(summary.getTotalPaidWorkHours() + (workHours * multiplier));
                break;
        }

        // 총 근무 시간과 근무 일수 업데이트
        summary.setTotalWorkHours(summary.getTotalWorkHours() + workHours);
        summary.setTotalWorkDays(summary.getNormalWorkDays() + summary.getHalfWorkDays() + summary.getHolidayWorkDays());

        // 야근 시간 계산 시간 당 x2로 계산
        if (attendanceRecord.getCheckOutTime().isAfter(LocalTime.parse("18:00"))) {
            double overtimeHours = LocalTime.parse("18:00").until(attendanceRecord.getCheckOutTime(), java.time.temporal.ChronoUnit.HOURS);
            summary.setOvertimeHours(summary.getOvertimeHours() + (Math.max(0, overtimeHours))*2);
            summary.setTotalPaidWorkHours(summary.getTotalPaidWorkHours() + summary.getOvertimeHours());
        }

        monthlyAttendanceSummaryRepository.save(summary);
    }


    public void delete(Long id) {
        Optional<AttendanceRecordEntity> entity = attendanceRecordRepository.findById(id);
        if(entity.isPresent()) {
            boolean alreadyCounted = isAlreadyCounted(entity.get().getEmployee(), entity.get().getDate());
            deleteMonthlySummary(entity.get(),alreadyCounted);
        }
        attendanceRecordRepository.deleteById(id);

    }

    private void deleteMonthlySummary(AttendanceRecordEntity attendanceRecord,boolean alreadyCounted) {
        LocalDate recordDate = attendanceRecord.getDate();
        YearMonth month = YearMonth.from(recordDate);
        Optional<MonthlyAttendanceSummaryEntity> summary = monthlyAttendanceSummaryRepository.findByEmployeeAndMonth(
                attendanceRecord.getEmployee(),
                month.atDay(1)  // Month의 첫 번째 날로 설정
        );

        if(summary.isPresent()) {
            // 근태 유형에 따른 요약 데이터 업데이트
            double workHours = attendanceRecord.getTotalWorkHours(); //삭제할 날의 총 근무 시간을 가져옴
            double multiplier = attendanceRecord.getAttendance().getMultiplier(); //근무 배수

            // 요약 데이터 업데이트 -> 다시 천천히..
            switch (attendanceRecord.getAttendance().getType()) {
                case NORMAL: //정상 근무
                    if (!alreadyCounted) {
                        if (workHours + 1 >= 8) {
                            summary.get().setNormalWorkDays(summary.get().getNormalWorkDays() - 1);
                        } else if (workHours + 0.5 >= 4) {
                            summary.get().setHalfWorkDays(summary.get().getHalfWorkDays() - 0.5);
                        }
                    }
                    summary.get().setTotalPaidWorkHours(summary.get().getTotalPaidWorkHours() - (workHours * multiplier));
                    break;
                case ABSENCE: //조퇴 , 결근
                    if (!alreadyCounted) {
                        summary.get().setTotalAbsenceDays(summary.get().getTotalAbsenceDays() - 1);
                    }
                    summary.get().setTotalPaidWorkHours(summary.get().getTotalPaidWorkHours() - (workHours * multiplier));
                    break;
                case SPECIAL: //특수 근무 -> 시간당 1.5배
                    summary.get().setSpecialWorkDays(summary.get().getSpecialWorkDays() - 1);
                    summary.get().setTotalPaidWorkHours(summary.get().getTotalPaidWorkHours() - (workHours * multiplier));
                    break;
                case HOLIDAY: //휴일 근무
                    if (!alreadyCounted) {
                        summary.get().setHolidayWorkDays(summary.get().getHolidayWorkDays() - 1);
                    }
                    summary.get().setTotalPaidWorkHours(summary.get().getTotalPaidWorkHours() - (workHours * multiplier));
                    break;
            }

            // 총 근무 시간과 근무 일수 업데이트
            summary.get().setTotalWorkHours(summary.get().getTotalWorkHours() - workHours);
            summary.get().setTotalWorkDays(summary.get().getNormalWorkDays() + summary.get().getHalfWorkDays() + summary.get().getHolidayWorkDays());

            // 야근 시간 계산 시간 당 x2로 계산
            if (attendanceRecord.getCheckOutTime().isAfter(LocalTime.parse("18:00"))) {
                double overtimeHours = LocalTime.parse("18:00").until(attendanceRecord.getCheckOutTime(), java.time.temporal.ChronoUnit.HOURS);
                summary.get().setOvertimeHours(summary.get().getOvertimeHours() - (Math.max(0, overtimeHours)) * 2);
                summary.get().setTotalPaidWorkHours(summary.get().getTotalPaidWorkHours() - summary.get().getOvertimeHours());
            }

            monthlyAttendanceSummaryRepository.save(summary.get());
        }
    }

    public List<AttendanceRecordEntity> searchRecords(String employeeName, LocalDate startDate, LocalDate endDate, Long attendanceId) {
        return attendanceRecordRepository.findByCriteria(employeeName, startDate, endDate, attendanceId);
    }


    public List<AttendanceRecordDTO> getAttendanceRecordsByEmployeeAndMonth(Long employeeId, int month) {
        List<AttendanceRecordEntity> attendanceRecordEntityList = attendanceRecordRepository.findByEmployeeIdAndMonth(employeeId,month);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd"); // 날짜 포맷
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); // 시간 포맷
        DecimalFormat decimalFormat = new DecimalFormat("0.#"); // 소수점 아래 0 제거 포맷

        return attendanceRecordEntityList.stream().map(attedance -> {
            AttendanceRecordDTO dto = new AttendanceRecordDTO();
            dto.setRecordId(attedance.getRecordId());
            dto.setEmployeeId(attedance.getEmployee().getEmployeeId());
            dto.setEmployeeName(attedance.getEmployee().getName());
            dto.setDate(attedance.getDate().format(dateFormatter));
            dto.setCheckInTime(attedance.getCheckInTime().format(timeFormatter));
            dto.setCheckOutTime(attedance.getCheckOutTime().format(timeFormatter));
            dto.setAttendanceType(attedance.getAttendance().getName());
            dto.setNotes(attedance.getNotes());
            // 소수점 아래가 0이면 소수점 없이 출력
            double totalWorkHours =attedance.getTotalWorkHours();
            dto.setTotalWorkHours(decimalFormat.format(totalWorkHours));

            return dto;
        }).collect(Collectors.toList());
    }


    public AttendanceRecordEntity recordAttendance(Long employeeId) {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek(); // 요일 가져오기

        boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);

        YearMonth currentMonth = YearMonth.from(today); // YearMonth로 변환
        EmployeeEntity employee = employeeService.getEmployeeById(employeeId);

        // 요일에 따라 AttendanceType 설정
        AttendanceType attendanceType = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)
                ? AttendanceType.HOLIDAY // 주말인 경우 HOLIDAY 설정
                : AttendanceType.NORMAL; // 평일인 경우 NORMAL 설정

        AttendanceEntity attendance = attendanceService.findByAttendanceType(attendanceType);

        // 오늘 날짜의 근태 기록 조회
        AttendanceRecordEntity record = attendanceRecordRepository.findByEmployeeAndDate(employee, today)
                .stream().findFirst().orElse(null);


        LocalTime now = LocalTime.now().withSecond(0).withNano(0);

        if (record == null) {
            // 출근 기록 생성
            record = new AttendanceRecordEntity();
            record.setEmployee(employee);
            record.setDate(today);
            record.setCheckInTime(now);
            record.setAttendance(attendance); // Attendance 설정
            attendanceRecordRepository.save(record);
        } else if (record.getCheckOutTime() == null) {
            // 퇴근 시간 기록
            record.setCheckOutTime(now);
            record.setAttendance(attendance); // Attendance 설정
            record.setTotalWorkHours(calculateWorkHours(record.getCheckInTime(), record.getCheckOutTime()));
            attendanceRecordRepository.save(record);

            // 월별 근태 요약 업데이트
            boolean alreadyCounted = isAlreadyCounted(employee, today);
            updateMonthlySummary(record, alreadyCounted);

            salaryCalculationService.updateMonthlyDeductions(employee.getEmployeeId(), currentMonth);
        } else {
            throw new IllegalStateException("오늘 근태 기록이 이미 완료되었습니다.");
        }

        return record;
    }

    private double calculateWorkHours(LocalTime checkInTime, LocalTime checkOutTime) {
        // 총 근무 시간 계산
        double totalHours = (double) java.time.Duration.between(checkInTime, checkOutTime).toMinutes() / 60;

        // 점심시간 제외 로직 추가
        if (totalHours >= 4 && totalHours < 8) {
            totalHours -= 0.5; // 4시간 이상 8시간 미만 근무 시 30분 제외
        } else if (totalHours >= 8) {
            totalHours -= 1; // 8시간 이상 근무 시 1시간 제외
        }

        // 유효성 검사: 퇴근 시간이 출근 시간보다 늦어야 함
        if (totalHours < 0) {
            throw new IllegalArgumentException("퇴근 시간은 출근 시간보다 늦어야 합니다.");
        }

        return totalHours;
    }

}
