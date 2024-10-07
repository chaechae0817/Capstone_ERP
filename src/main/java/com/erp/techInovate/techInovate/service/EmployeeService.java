package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.repository.EmployeeRepository;
import com.erp.techInovate.techInovate.specification.EmployeeSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //controller와 같이. final 멤버변수 생성자 만드는 역할
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Optional<EmployeeEntity> login(String employeeNumber, String email) {
        return employeeRepository.findByEmployeeNumberAndEmail(employeeNumber, email);
    }
    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }


    public EmployeeEntity saveEmployee(EmployeeEntity entity) {
        return employeeRepository.save(entity);
    }

    public List<EmployeeEntity> searchEmployees(String name, String ssn, String position, String status,
                                                String department, LocalDate hireDate, String contactInfo,
                                                String email, LocalDate birthDate, String address,
                                                String experience, String accountNumber, String bank) {
        Specification<EmployeeEntity> spec = Specification.where(EmployeeSpecifications.hasName(name))
                .and(EmployeeSpecifications.hasSsn(ssn))
                .and(EmployeeSpecifications.hasPosition(position))
                .and(EmployeeSpecifications.hasStatus(status))
                .and(EmployeeSpecifications.hasDepartment(department))
                .and(EmployeeSpecifications.hasHireDate(hireDate))
                .and(EmployeeSpecifications.hasContactInfo(contactInfo))
                .and(EmployeeSpecifications.hasEmail(email))
                .and(EmployeeSpecifications.hasBirthDate(birthDate))
                .and(EmployeeSpecifications.hasAddress(address))
                .and(EmployeeSpecifications.hasExperience(experience))
                .and(EmployeeSpecifications.hasAccountNumber(accountNumber))
                .and(EmployeeSpecifications.hasBank(bank));

        return employeeRepository.findAll(spec);
    }
    // 특정 사원 정보를 조회하는 메소드
    public EmployeeEntity getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null); // Optional 처리
    }

    // 사원 정보를 업데이트하는 메소드
    public void updateEmployee(EmployeeEntity employee) {
        employeeRepository.save(employee); // save 메소드는 존재하는 엔티티를 업데이트합니다.
    }

}
