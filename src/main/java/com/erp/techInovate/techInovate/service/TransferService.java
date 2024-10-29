package com.erp.techInovate.techInovate.service;

import com.erp.techInovate.techInovate.dto.TransferDTO;
import com.erp.techInovate.techInovate.entity.TransferEntity;
import com.erp.techInovate.techInovate.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    // 발령 정보 저장
    public void save(TransferEntity transfer) {
        transferRepository.save(transfer);
    }

    // 발령 리스트 조회
    public List<TransferDTO> findAll() {
        return transferRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 발령 엔티티를 DTO로 변환
    private TransferDTO convertToDTO(TransferEntity transfer) {
        TransferDTO dto = new TransferDTO();
        dto.setTransferId(transfer.getTransferId());
        dto.setEmployeeId(transfer.getEmployee().getEmployeeId());
        dto.setEmployeeName(transfer.getEmployee().getName());
        dto.setFromDepartmentName(transfer.getFromDepartment().getName());
        dto.setToDepartmentName(transfer.getToDepartment().getName());
        dto.setFromPositionName(transfer.getFromPosition().getName());
        dto.setToPositionName(transfer.getToPosition().getName());
        dto.setTransferDate(transfer.getTransferDate());
        dto.setPersonnelAppointment(transfer.getPersonnelAppointment());
        return dto;
    }

    // 발령 ID로 조회
    public TransferEntity findById(Long transferId) {
        return transferRepository.findById(transferId).orElseThrow();
    }

    // 발령 삭제
    public void deleteById(Long transferId) {
        transferRepository.deleteById(transferId);
    }

    public List<TransferDTO> searchTransfers(String employeeName, Long toDepartmentId, Long toPositionId, LocalDate transferDate, String personnelAppointment) {
        List<TransferEntity> transfers = transferRepository.searchTransfers(employeeName, toDepartmentId, toPositionId, transferDate, personnelAppointment);
        return transfers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
