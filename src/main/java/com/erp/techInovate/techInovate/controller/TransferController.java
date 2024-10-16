package com.erp.techInovate.techInovate.controller;


import com.erp.techInovate.techInovate.dto.TransferDTO;
import com.erp.techInovate.techInovate.entity.TransferEntity;
import com.erp.techInovate.techInovate.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RequestMapping("/transfer")
@Controller
public class TransferController {

    @Autowired
    private TransferService TransferService;

    @GetMapping("/list")
    public String listTransfers(Model model) {
        List<TransferEntity> transfers = TransferService.findAll();
        model.addAttribute("transfers", transfers);
        return "transfer/list"; // 리스트 페이지의 뷰 이름
    }

    @GetMapping("/new")
    public String newTransfer(Model model) {
        model.addAttribute("transfer", new TransferDTO());
        return "transfer/new"; // 신규 입력 페이지의 뷰 이름
    }

    @PostMapping("/new")
    public String createTransfer(@ModelAttribute TransferDTO transferDTO) {
        TransferEntity transfer = new TransferEntity();
        // DTO에서 엔티티로 데이터 복사
        transfer.setEmployeeId(transferDTO.getEmployeeId());
        transfer.setPersonnelAppointment(transferDTO.getPersonnelAppointment());
        transfer.setFromDepartment(transferDTO.getFromDepartment());
        transfer.setToDepartment(transferDTO.getToDepartment());
        transfer.setFromPosition(transferDTO.getFromPosition());
        transfer.setToPosition(transferDTO.getToPosition());
        transfer.setTransferDate(transferDTO.getTransferDate());

        TransferService.save(transfer);
        return "redirect:/transfer/list";
    }

    @GetMapping("/update/{id}")
    public String updateTransfer(@PathVariable Long id, Model model) {
        TransferEntity transfer = TransferService.findById(id);
        model.addAttribute("transfer", transfer);
        return "transfer/update"; // 수정 페이지의 뷰 이름
    }

    @PostMapping("/update")
    public String updateTransfer(@ModelAttribute TransferEntity transfer) {
        TransferService.save(transfer);
        return "redirect:/transfer/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteTransfer(@PathVariable Long id) {
        TransferService.deleteById(id);
        return "redirect:/transfer/list";
    }
}
