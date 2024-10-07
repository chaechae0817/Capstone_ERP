package com.erp.techInovate.techInovate.controller;

import com.erp.techInovate.techInovate.entity.PositionEntity;
import com.erp.techInovate.techInovate.entity.DepartmentEntity;
import com.erp.techInovate.techInovate.service.PositionService; // 서비스 클래스 필요
import com.erp.techInovate.techInovate.service.DepartmentService; // 서비스 클래스 필요
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    private PositionService positionService; // 포지션 서비스
    @Autowired
    private DepartmentService departmentService; // 부서 서비스

    @GetMapping("/position/list")
    public String getPositions(Model model) {
        List<PositionEntity> positions = positionService.getAllPositions();
        model.addAttribute("positions", positions);
        return "positionList"; // 포지션 목록 페이지로 이동
    }

    @GetMapping("/department/list")
    public String getDepartments(Model model) {
        List<DepartmentEntity> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        return "departmentList"; // 부서 목록 페이지로 이동
    }

    @PostMapping("/addPosition")
    @ResponseBody
    public ResponseEntity<Void> addPosition(@RequestBody PositionEntity position) {
        positionService.savePosition(position); // 포지션 저장
        return ResponseEntity.ok().build(); // 성공 응답
    }

    @PostMapping("/positions/delete")
    public String deletePosition(@RequestParam Long id) {
        positionService.deletePosition(id); // 포지션 삭제
        return "redirect:/positions"; // 포지션 목록으로 리다이렉트
    }
    @PostMapping("/addDepartment")
    @ResponseBody
    public ResponseEntity<Void> addDepartment(@RequestBody DepartmentEntity department) {
        departmentService.saveDepartment(department); // 부서 저장
        return ResponseEntity.ok().build(); // 성공 응답
    }

    @PostMapping("/departments/delete")
    public String deleteDepartment(@RequestParam Long id) {
        departmentService.deleteDepartment(id); // 부서 삭제
        return "redirect:/departments"; // 부서 목록으로 리다이렉트
    }

}
