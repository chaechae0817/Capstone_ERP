package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.dto.EmployeeDTO;
import com.erp.techInovate.techInovate.entity.DepartmentEntity;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.PositionEntity;
import com.erp.techInovate.techInovate.repository.DepartmentRepository;
import com.erp.techInovate.techInovate.repository.EmployeeRepository;
import com.erp.techInovate.techInovate.repository.PositionRepository;
import com.erp.techInovate.techInovate.specification.EmployeeSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //controller와 같이. final 멤버변수 생성자 만드는 역할
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final PositionRepository positionRepository;

    private final DepartmentRepository departmentRepository;

    public Optional<EmployeeEntity> login(String employeeNumber, String email) {
        return employeeRepository.findByEmployeeNumberAndEmail(employeeNumber, email);
    }
    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public EmployeeEntity saveEmployee(EmployeeDTO employeeDTO, String fileName) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setEmployeeId(employeeDTO.getEmployeeId());
        employeeEntity.setEmployeeNumber(employeeDTO.getEmployeeNumber());
        employeeEntity.setName(employeeDTO.getName());
        employeeEntity.setSsn(employeeDTO.getSsn());

        // PositionEntity와 DepartmentEntity를 가져와서 설정
        PositionEntity position = positionRepository.findById(employeeDTO.getPositionId())
                .orElseThrow(() -> new RuntimeException("Position not found"));
        employeeEntity.setPosition(position);

        DepartmentEntity department = departmentRepository.findById(employeeDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        employeeEntity.setDepartment(department);


        employeeEntity.setStatus(employeeDTO.getStatus());
        employeeEntity.setHireDate(employeeDTO.getHireDate());
        employeeEntity.setContactInfo(employeeDTO.getContactInfo());
        employeeEntity.setEmail(employeeDTO.getEmail());
        employeeEntity.setBirthDate(employeeDTO.getBirthDate());
        employeeEntity.setAddress(employeeDTO.getAddress());
        employeeEntity.setExperience(employeeDTO.getExperience());
        employeeEntity.setAccountNumber(employeeDTO.getAccountNumber());
        employeeEntity.setBank(employeeDTO.getBank());
        employeeEntity.setPhoto("/files/"+fileName);
        employeeEntity.setSalary(employeeDTO.getSalary()); // 연봉 필드 설정
        employeeEntity.setFamily(employeeDTO.getFamily());
        return employeeRepository.save(employeeEntity);
    }

    public String write(MultipartFile file) throws Exception{

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";

        String fileName =  file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);

        Files.createDirectories(Paths.get(projectPath));
        file.transferTo(saveFile);

//        boardDTO.setFilePath(file);  // 파일 경로 설정

        return fileName;
    }

    public List<EmployeeEntity> searchEmployees(String name,Long positionId, String status,
                                                Long departmentId, LocalDate hireDate, String contactInfo,
                                                String email, LocalDate birthDate, String address,
                                                String experience) {
        Specification<EmployeeEntity> spec = Specification.where(EmployeeSpecifications.hasName(name))
                .and(EmployeeSpecifications.hasPosition(positionId)) // positionId로 변경
                .and(EmployeeSpecifications.hasStatus(status))
                .and(EmployeeSpecifications.hasDepartment(departmentId)) // departmentId로 변경
                .and(EmployeeSpecifications.hasHireDate(hireDate))
                .and(EmployeeSpecifications.hasContactInfo(contactInfo))
                .and(EmployeeSpecifications.hasEmail(email))
                .and(EmployeeSpecifications.hasBirthDate(birthDate))
                .and(EmployeeSpecifications.hasAddress(address))
                .and(EmployeeSpecifications.hasExperience(experience));
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
    public EmployeeEntity findById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }

    public void save(EmployeeEntity employee) {
        employeeRepository.save(employee);
    }
    // 모든 직원 정보 조회 메서드
    public List<EmployeeEntity> findAll() {
        return employeeRepository.findAll();
    }

}
