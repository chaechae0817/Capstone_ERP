package com.erp.techInovate.techInovate.service;


import com.erp.techInovate.techInovate.entity.CertificateIssueEntity;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.repository.CertificateIssueRepository;
import com.erp.techInovate.techInovate.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CertificateIssueService {

    @Autowired
    private CertificateIssueRepository certificateIssueRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<CertificateIssueEntity> getAllCertificateIssues() {
        return certificateIssueRepository.findAll();
    }

    // 발급 내역 저장 메서드
    public void recordIssue(Long employeeId, String certificateType) {
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        CertificateIssueEntity issue = new CertificateIssueEntity();
        issue.setEmployee(employee);
        issue.setIssueDate(LocalDate.now().atStartOfDay());
        issue.setCertificateType(certificateType);
        certificateIssueRepository.save(issue);
    }
}