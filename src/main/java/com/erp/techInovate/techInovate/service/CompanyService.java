package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.dto.CompanyDTO;
import com.erp.techInovate.techInovate.entity.CompanyEntity;
import com.erp.techInovate.techInovate.repository.CompanyRepository;
import com.mysql.cj.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public void saveCompany(CompanyDTO companyDTO) {
        // 회사가 이미 존재하는지 확인
        if (companyRepository.count() == 0) {
            CompanyEntity company = new CompanyEntity();
            company.setName(companyDTO.getName());
            company.setAddress(companyDTO.getAddress());
            company.setContactNumber(companyDTO.getContactNumber());
            company.setEmail(companyDTO.getEmail());
            company.setRegistrationNumber(companyDTO.getRegistrationNumber());
            company.setRepresentativeName(companyDTO.getRepresentativeName());
            company.setBusinessType(companyDTO.getBusinessType());
            companyRepository.save(company);
        } else {
            throw new RuntimeException("회사는 이미 등록되어 있습니다. 수정만 가능합니다.");
        }
    }

    public Optional<CompanyEntity> getCompany() {
        return companyRepository.findAll().stream().findFirst(); // 단 하나의 회사만 반환
    }

    public CompanyEntity updateCompany(Long id, CompanyDTO companyDTO) {
        CompanyEntity company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setName(companyDTO.getName());
        company.setAddress(companyDTO.getAddress());
        company.setContactNumber(companyDTO.getContactNumber());
        company.setEmail(companyDTO.getEmail());
        company.setRegistrationNumber(companyDTO.getRegistrationNumber());
        company.setRepresentativeName(companyDTO.getRepresentativeName());
        company.setBusinessType(companyDTO.getBusinessType());
        return companyRepository.save(company);
    }

    public Optional<CompanyEntity> getCompanyById(Long id){
        return companyRepository.findById(id);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
