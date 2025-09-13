package com.erp.service;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.erp.dto.LeaveApplicationHRDto;
import com.erp.model.LeaveApplication;
import com.erp.repository.LeaveApplicationRepository;

@Service
public class HRLeaveService {

    private LeaveApplicationRepository leaveRepo;

    public HRLeaveService(LeaveApplicationRepository leaveRepo) {
        this.leaveRepo = leaveRepo;
    };

    public List<LeaveApplicationHRDto> getAllLeaves() {
        List<LeaveApplication> leaves = leaveRepo.findAll();

        return leaves.stream().map(leave -> {
            LeaveApplicationHRDto dto = new LeaveApplicationHRDto();

            dto.setLeaveApplicationId(leave.getLeaveApplicationId());
            dto.setEmployeeId(leave.getEmployee().getEmpId());
            dto.setEmployeeName(leave.getEmployee().getFirst_name() + " " + leave.getEmployee().getLast_name());
            dto.setEmployeeCode(leave.getEmployee().getEmpCode());

            dto.setLeaveTypeId(leave.getLeaveType().getLeavetype_id());
            dto.setLeaveTypeName(leave.getLeaveType().getLeave_type());

            dto.setFromDate(leave.getFromDate());
            dto.setToDate(leave.getToDate());
            dto.setSessionFrom(leave.getSessionFrom());
            dto.setSessionTo(leave.getSessionTo());
            dto.setContactDetails(leave.getContactDetails());
            dto.setReason(leave.getReason());
            dto.setStatus(leave.getStatus());
            dto.setDateCreated(leave.getDateCreated());

            dto.setApplyToId(leave.getApplyTo().getEmpId());
            dto.setApplyToName(leave.getApplyTo().getFirst_name() + " " + leave.getApplyTo().getLast_name());

            // calculating leave days
            long days = ChronoUnit.DAYS.between(leave.getFromDate(), leave.getToDate()) + 1;
            dto.setQty(days);

            dto.setAttachmentPath(leave.getAttachmentPath());

            return dto;
        }).toList();
    }

}
