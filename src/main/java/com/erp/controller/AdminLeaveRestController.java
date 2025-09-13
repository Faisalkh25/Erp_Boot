package com.erp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.model.LeaveApplication;
import com.erp.repository.LeaveApplicationRepository;

@RestController
@RequestMapping("/api/admin/leaves")
public class AdminLeaveRestController {

    private final LeaveApplicationRepository leaveRepo;

    public AdminLeaveRestController(LeaveApplicationRepository leaveRepo) {
        this.leaveRepo = leaveRepo;
    }

    @PutMapping("/{leaveId}/status/{status}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> updateLeaveStatus(@PathVariable int leaveId, @PathVariable String status) {

        LeaveApplication leave = leaveRepo.findById(leaveId).orElseThrow(() -> new RuntimeException("Leave not found"));

        if (!status.equalsIgnoreCase("APPROVED") &&
                !status.equalsIgnoreCase("REJECTED") &&
                !status.equalsIgnoreCase("PENDING")) {
            return ResponseEntity.badRequest().body("Invalid status");
        }

        leave.setStatus(status.toUpperCase());
        leaveRepo.save(leave);

        return ResponseEntity.ok("Leave status updated to " + status);
    }

}
