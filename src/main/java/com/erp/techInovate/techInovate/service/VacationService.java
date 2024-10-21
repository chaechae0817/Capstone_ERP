package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.dto.VacationDTO;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.LeaveEntity;
import com.erp.techInovate.techInovate.entity.VacationEntity;
import com.erp.techInovate.techInovate.repository.EmployeeRepository;
import com.erp.techInovate.techInovate.repository.LeaveRepository;
import com.erp.techInovate.techInovate.repository.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VacationService {

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    public VacationEntity applyVacation(VacationDTO vacationDTO) {
        Optional<EmployeeEntity> employee = employeeRepository.findById(vacationDTO.getEmployeeId());
        Optional<LeaveEntity> leaveType = leaveRepository.findByName(vacationDTO.getVacationTypeName());

        if (employee.isPresent() && leaveType.isPresent()) {
            VacationEntity vacation = new VacationEntity();
            vacation.setEmployee(employee.get());
            vacation.setVacationType(leaveType.get());
            vacation.setStartDate(vacationDTO.getStartDate());
            vacation.setEndDate(vacationDTO.getEndDate());
            vacation.setReason(vacationDTO.getReason());

            return vacationRepository.save(vacation);
        } else {
            throw new IllegalArgumentException("Invalid employee ID or vacation type");
        }
    }

    public List<VacationEntity> getAllVacations() {
        return vacationRepository.findAll();
    }
}
