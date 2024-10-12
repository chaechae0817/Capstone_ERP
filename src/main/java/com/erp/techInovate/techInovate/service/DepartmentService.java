package com.erp.techInovate.techInovate.service;


import com.erp.techInovate.techInovate.entity.DepartmentEntity;
import com.erp.techInovate.techInovate.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<DepartmentEntity> getAllDepartments(){
        return departmentRepository.findAll();
    }

    public DepartmentEntity saveDepartment(DepartmentEntity department){
        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }



}
