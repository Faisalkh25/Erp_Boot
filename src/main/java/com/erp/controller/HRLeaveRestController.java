package com.erp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.repository.LeaveApplicationRepository;
import com.erp.service.HRLeaveService;
import com.erp.dto.LeaveApplicationHRDto;
import com.erp.model.LeaveApplication;

@RestController
@RequestMapping("/api/hr/leaves")
public class HRLeaveRestController {

    private final HRLeaveService hrLeaveService;
    private final LeaveApplicationRepository leaveRepo;

    public HRLeaveRestController(HRLeaveService hrLeaveService, LeaveApplicationRepository leaveRepo) {
        this.hrLeaveService = hrLeaveService;
        this.leaveRepo = leaveRepo;
    }

    @GetMapping("/leave-all")
    public ResponseEntity<List<LeaveApplicationHRDto>> getAllLeaves() {
        return ResponseEntity.ok(hrLeaveService.getAllLeaves());
    }

    @PutMapping("/{leaveId}/approve")
    public ResponseEntity<?> approveLeave(@PathVariable int leaveId) {
        LeaveApplication leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        // only allow approve if status is "pending"
        if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("only pending leaves can be  approved by HR");
        }

        leave.setStatus("APPROVED");
        leaveRepo.save(leave);
        return ResponseEntity.ok("Leave Approved successfully");
    }

    @PutMapping("/{leaveId}/reject")
    public ResponseEntity<?> rejectLeave(@PathVariable int leaveId) {
        LeaveApplication leave = leaveRepo.findById(leaveId).orElseThrow(() -> new RuntimeException("Leave not found"));

        // only allow reject if status is pending
        if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only pending leaves can be rejected by HR");
        }

        leave.setStatus("REJECTED");
        leaveRepo.save(leave);
        return ResponseEntity.ok("Leave Rejected Successfully");
    }

}
