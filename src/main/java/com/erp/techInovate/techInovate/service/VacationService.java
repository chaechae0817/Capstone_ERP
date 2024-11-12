package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.dto.VacationDTO;
import com.erp.techInovate.techInovate.entity.*;
import com.erp.techInovate.techInovate.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VacationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private VacationRepository vacationRepository;
    public void applyForVacation(VacationDTO vacationDTO) {
        System.out.println("applyForVacation called with Employee ID: " + vacationDTO.getEmployeeId());

        if (vacationDTO.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID must not be null");
        }
        if (vacationDTO.getLeaveItemName() == null) {
            throw new IllegalArgumentException("Leave Item ID must not be null");
        }

        EmployeeEntity employee = employeeRepository.findById(vacationDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        LeaveEntity leaveItem = leaveRepository.findByName(vacationDTO.getLeaveItemName())
                .orElseThrow(() -> new RuntimeException("Leave item not found")); // 이름으로 검색

        VacationEntity vacation = new VacationEntity();
        vacation.setEmployee(employee);
        vacation.setLeaveItem(leaveItem);
        vacation.setStartDate(vacationDTO.getStartDate());
        vacation.setEndDate(vacationDTO.getEndDate());
        vacation.setReason(vacationDTO.getReason());
        // days 필드 설정 - 예를 들어, 기본값으로 0을 설정
        vacation.setDays(0);
        vacation.setStatus("PENDING");
        vacationRepository.save(vacation);
        System.out.println("Vacation saved with ID: " + vacation.getId());
    }

    public List<LeaveEntity> getAllLeaveItems() {
        return leaveRepository.findAll();
    }
    public List<VacationDTO> getAllVacationApplications() {
        List<VacationEntity> vacations = vacationRepository.findByStatus("PENDING");
        return vacations.stream().map(vacation -> {
            VacationDTO dto = new VacationDTO();
            dto.setId(vacation.getId()); // VacationEntity의 ID를 DTO에 설정
            dto.setEmployeeId(vacation.getEmployee().getEmployeeId());
            dto.setName(vacation.getEmployee().getName());
            dto.setPosition(vacation.getEmployee().getPosition().getName());
            dto.setDepartment(vacation.getEmployee().getDepartment().getName());
            dto.setLeaveItemName(vacation.getLeaveItem().getName());
            dto.setReason(vacation.getReason());
            dto.setStartDate(vacation.getStartDate());
            dto.setEndDate(vacation.getEndDate());
            return dto;
        }).collect(Collectors.toList());
    }



    public void approveVacation(Long vacationId) {
        VacationEntity vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new RuntimeException("Vacation not found"));
        vacation.setStatus("APPROVED"); // 상태를 "APPROVED"로 설정
        vacationRepository.save(vacation); // 변경 사항 저장
    }

    // deleteVacation 메서드 추가
    public void deleteVacation(Long vacationId) {
        vacationRepository.deleteById(vacationId); // 해당 휴가 신청 삭제
    }

    public List<VacationDTO> getConfirmedVacations() {
        List<VacationEntity> confirmedVacations = vacationRepository.findByStatus("APPROVED"); // 'APPROVED' 상태의 휴가 조회
        return confirmedVacations.stream().map(vacation -> {
            VacationDTO dto = new VacationDTO();
            dto.setEmployeeId(vacation.getEmployee().getEmployeeId());
            dto.setName(vacation.getEmployee().getName());
            dto.setPosition(vacation.getEmployee().getPosition().getName());
            dto.setDepartment(vacation.getEmployee().getDepartment().getName());
            dto.setLeaveItemName(vacation.getLeaveItem().getName());
            dto.setReason(vacation.getReason());
            dto.setStartDate(vacation.getStartDate());
            dto.setEndDate(vacation.getEndDate());
            return dto;
        }).collect(Collectors.toList());
    }
}