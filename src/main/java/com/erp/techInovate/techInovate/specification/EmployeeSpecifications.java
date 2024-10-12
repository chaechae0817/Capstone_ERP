package com.erp.techInovate.techInovate.specification;

import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EmployeeSpecifications {

    // 이름으로 검색
    public static Specification<EmployeeEntity> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction(); // 조건이 없을 경우 항상 true
            }
            return criteriaBuilder.equal(root.get("name"), name);
        };
    }

    //사원번호로 검색
    public static Specification<EmployeeEntity> hasEmployeeNumber(String employeeNumber) {
        return (root, query, criteriaBuilder) -> {
            if (employeeNumber == null || employeeNumber.isEmpty()) {
                return criteriaBuilder.conjunction(); // 조건이 없을 경우 항상 true
            }
            return criteriaBuilder.equal(root.get("employeeNumber"), employeeNumber);
        };
    }

    // 주민번호로 검색
    public static Specification<EmployeeEntity> hasSsn(String ssn) {
        return (root, query, criteriaBuilder) -> {
            if (ssn == null || ssn.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("ssn"), ssn);
        };
    }

    // 직급으로 검색
    public static Specification<EmployeeEntity> hasPosition(Long positionId) {
        return (root, query, criteriaBuilder) ->
                positionId == null ? null : criteriaBuilder.equal(root.join("position").get("id"), positionId);
    }


    // 직원 구분으로 검색
    public static Specification<EmployeeEntity> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    // 부서로 검색
    public static Specification<EmployeeEntity> hasDepartment(Long departmentId) {
        return (root, query, criteriaBuilder) ->
                departmentId == null ? null : criteriaBuilder.equal(root.join("department").get("id"), departmentId);
    }


    // 입사일로 검색
    public static Specification<EmployeeEntity> hasHireDate(LocalDate hireDate) {
        return (root, query, criteriaBuilder) -> {
            if (hireDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("hireDate"), hireDate);
        };
    }

    // 연락처로 검색
    public static Specification<EmployeeEntity> hasContactInfo(String contactInfo) {
        return (root, query, criteriaBuilder) -> {
            if (contactInfo == null || contactInfo.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("contactInfo"), contactInfo);
        };
    }

    // 이메일로 검색
    public static Specification<EmployeeEntity> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }

    // 생년월일로 검색
    public static Specification<EmployeeEntity> hasBirthDate(LocalDate birthDate) {
        return (root, query, criteriaBuilder) -> {
            if (birthDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("birthDate"), birthDate);
        };
    }

    // 주소로 검색
    public static Specification<EmployeeEntity> hasAddress(String address) {
        return (root, query, criteriaBuilder) -> {
            if (address == null || address.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("address"), address);
        };
    }

    // 경력으로 검색
    public static Specification<EmployeeEntity> hasExperience(String experience) {
        return (root, query, criteriaBuilder) -> {
            if (experience == null || experience.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("experience"), experience);
        };
    }

    // 계좌 번호로 검색
    public static Specification<EmployeeEntity> hasAccountNumber(String accountNumber) {
        return (root, query, criteriaBuilder) -> {
            if (accountNumber == null || accountNumber.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("accountNumber"), accountNumber);
        };
    }

    // 은행으로 검색
    public static Specification<EmployeeEntity> hasBank(String bank) {
        return (root, query, criteriaBuilder) -> {
            if (bank == null || bank.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("bank"), bank);
        };
    }
}