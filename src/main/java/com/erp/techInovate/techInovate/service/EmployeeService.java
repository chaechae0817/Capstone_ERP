package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor //controller와 같이. final 멤버변수 생성자 만드는 역할
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }


    public EmployeeEntity saveEmployee(EmployeeEntity entity) {
        return employeeRepository.save(entity);
    }

}
