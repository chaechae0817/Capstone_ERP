package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.dto.TransferDTO;
import com.erp.techInovate.techInovate.entity.DepartmentEntity;
import com.erp.techInovate.techInovate.entity.EmployeeEntity;
import com.erp.techInovate.techInovate.entity.PositionEntity;
import com.erp.techInovate.techInovate.entity.TransferEntity;
import com.erp.techInovate.techInovate.service.DepartmentService;
import com.erp.techInovate.techInovate.service.EmployeeService;
import com.erp.techInovate.techInovate.service.PositionService;
import com.erp.techInovate.techInovate.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RequiredArgsConstructor
@Controller
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    // 직원 선택 페이지 (employeeId를 선택하는 페이지)
    @GetMapping("/new")
    public String newTransferForm(Model model) {
        List<EmployeeEntity> employees = employeeService.findAll(); // 모든 직원 정보 가져오기
        model.addAttribute("employees", employees);                 // 직원 목록을 모델에 추가
        return "transfer/new";                                      // 직원 선택 페이지로 이동
    }


    // 직원 선택 후, 해당 직원의 정보와 부서, 직급 선택 페이지로 이동
    @GetMapping("/newselect")
    public String newTransferSelect(@RequestParam("employeeId") Long employeeId, Model model) {
        EmployeeEntity employee = employeeService.findById(employeeId);  // 선택된 직원 정보 가져오기
        List<PositionEntity> positions = positionService.getAllPositions(); // 직급 목록 가져오기
        List<DepartmentEntity> departments = departmentService.getAllDepartments(); // 부서 목록 가져오기

        model.addAttribute("employee", employee); // 선택된 직원 정보 모델에 추가
        model.addAttribute("positions", positions); // 직급 목록 모델에 추가
        model.addAttribute("departments", departments); // 부서 목록 모델에 추가

        return "transfer/newselect"; // 발령 정보 입력 페이지로 이동
    }


    // 발령 승인 처리
    @PostMapping("/approve")
    public String approveTransfer(@RequestParam("transferId") Long transferId) {
        TransferEntity transfer = transferService.findById(transferId);
        EmployeeEntity employee = transfer.getEmployee();

        // 발령된 부서와 직급으로 직원 정보 업데이트 ( 입력 시 할 행동)
        employee.setDepartment(transfer.getToDepartment());
        employee.setPosition(transfer.getToPosition());

        employeeService.save(employee);



        // 발령 항목 삭제
        transferService.deleteById(transferId);


        // 승인 후 리스트 페이지로 리다이렉트
        return "redirect:/transfer/list";
    }


    // 발령 신청 처리 (POST 요청)
    @PostMapping("/apply")
    public String applyTransfer(@ModelAttribute TransferDTO transferDTO) {
        TransferEntity transfer = new TransferEntity();

        // 직원 정보 가져오기
        EmployeeEntity employee = employeeService.findById(transferDTO.getEmployeeId());
        transfer.setEmployee(employee);

        // 현재 부서와 직급 설정 (EmployeeEntity로부터 가져옴)
        transfer.setFromDepartment(employee.getDepartment());
        transfer.setFromPosition(employee.getPosition());

        // 발령 부서와 직급 설정 (DTO로부터 가져옴)
        DepartmentEntity toDepartment = departmentService.findById(transferDTO.getToDepartmentId());
        PositionEntity toPosition = positionService.findById(transferDTO.getToPositionId());

        transfer.setToDepartment(toDepartment);
        transfer.setToPosition(toPosition);

        // 발령 구분 및 발령일 설정
        transfer.setPersonnelAppointment(transferDTO.getPersonnelAppointment());
        transfer.setTransferDate(transferDTO.getTransferDate());


        employee.setPosition(transfer.getToPosition());
        employee.setDepartment(transfer.getToDepartment());
        employeeService.save(employee);
        // 발령 정보 저장
        transferService.save(transfer);
        // 발령 신청 후 리스트 페이지로 리다이렉트
        return "redirect:/transfer/list";  // 리다이렉트 명시적으로 추가
    }

    // 발령 삭제 처리
    @PostMapping("/delete")
    public String deleteTransfer(@RequestParam("transferId") Long transferId) {
        TransferEntity transfer = transferService.findById(transferId);
        EmployeeEntity employee = transfer.getEmployee();
        //취소 시 할 행동
        employee.setDepartment(transfer.getFromDepartment());
        employee.setPosition(transfer.getFromPosition());

        employeeService.save(employee);

        transferService.deleteById(transferId); // 발령 ID로 발령 정보 삭제
        return "redirect:/transfer/list";       // 발령 목록 페이지로 리다이렉트
    }

    // 발령 리스트 페이지 (GET 요청)
    @GetMapping("/list")
    public String listTransfers(Model model) {
        List<TransferDTO> transfers = transferService.findAll();   // 모든 발령 정보 가져오기
        model.addAttribute("transfers", transfers);                // 발령 목록을 모델에 추가
        return "transfer/list";                                    // 발령 목록 페이지로 이동
    }

}
