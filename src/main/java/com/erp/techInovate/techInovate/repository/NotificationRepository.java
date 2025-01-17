package com.erp.techInovate.techInovate.repository;

import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity,Long> {

    List<NotificationEntity> findByEmployeeAndIsReadFalse(EmployeeEntity employee);

}
