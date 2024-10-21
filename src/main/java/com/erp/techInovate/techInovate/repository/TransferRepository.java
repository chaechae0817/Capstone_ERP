package com.erp.techInovate.techInovate.repository;


import com.erp.techInovate.techInovate.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Long> {

}
