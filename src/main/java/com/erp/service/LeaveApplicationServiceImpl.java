package com.erp.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.dto.LeaveApplicationDto;
import com.erp.model.LeaveApplication;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.LeaveApplicationRepository;
import com.erp.repository.LeaveTypeRepository;

@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    private final LeaveApplicationRepository leaveRepo;
    private final EmployeeRepository employeeRepo;
    private final LeaveTypeRepository leaveTypeRepo;

    @Value("${file.upload-dir.leaves}")
    private String leaveUploadDir;

    public LeaveApplicationServiceImpl(LeaveApplicationRepository leaveRepo, EmployeeRepository employeeRepo,
            LeaveTypeRepository leaveTypeRepo) {
        this.leaveRepo = leaveRepo;
        this.employeeRepo = employeeRepo;
        this.leaveTypeRepo = leaveTypeRepo;
    }

    @Override
    public LeaveApplication applyLeave(LeaveApplicationDto dto, String attachmentPath) {

        LeaveApplication leave = new LeaveApplication();
        leave.setEmployee(employeeRepo.findById(dto.getEmployeeId()).orElseThrow());
        leave.setLeaveType(leaveTypeRepo.findById(dto.getLeaveTypeId()).orElseThrow());
        leave.setSessionFrom(dto.getSessionFrom());
        leave.setSessionTo(dto.getSessionTo());
        leave.setFromDate(dto.getFromDate());
        leave.setToDate(dto.getToDate());
        leave.setContactDetails(dto.getContactDetails());
        leave.setApplyTo(employeeRepo.findById(dto.getApplyToId()).orElseThrow());
        if (dto.getCcEmployeeIds() != null && !dto.getCcEmployeeIds().isEmpty()) {
            leave.setCcEmployees(employeeRepo.findAllById(dto.getCcEmployeeIds()));
        } else {
            leave.setCcEmployees(new ArrayList<>());
        }
        leave.setReason(dto.getReason());
        leave.setAttachmentPath(attachmentPath);

        leave.setStatus("PENDING");

        return leaveRepo.save(leave);
    }

    // @Override
    // public List<LeaveApplication> getLeavesByEmployee(int employeeId) {
    // return leaveRepo.findByEmployeeEmpId(employeeId);
    // }

    @Override
    public List<LeaveApplicationDto> getLeavesByEmployee(int employeeId) {
        List<LeaveApplication> leaves = leaveRepo.findByEmployeeEmpId(employeeId);

        return leaves.stream().map(leave -> {
            LeaveApplicationDto dto = new LeaveApplicationDto();
            dto.setEmployeeId(leave.getEmployee().getEmpId());
            dto.setEmployeeName(leave.getEmployee().getFirst_name() + " " + leave.getEmployee().getLast_name());
            dto.setEmployeeCode(leave.getEmployee().getEmpCode());

            dto.setLeaveTypeId(leave.getLeaveType().getLeavetype_id());
            dto.setLeaveTypeName(leave.getLeaveType().getLeave_type());

            dto.setSessionFrom(leave.getSessionFrom());
            dto.setSessionTo(leave.getSessionTo());
            dto.setFromDate(leave.getFromDate());
            dto.setToDate(leave.getToDate());
            dto.setContactDetails(leave.getContactDetails());
            dto.setApplyToId(leave.getApplyTo().getEmpId());
            dto.setReason(leave.getReason());
            // if you want cc list
            dto.setCcEmployeeIds(
                    leave.getCcEmployees().stream().map(e -> e.getEmpId()).toList());

            dto.setStatus(leave.getStatus());

            return dto;
        }).toList();

    }

    // helper method to handle file uploads

    public String saveAttachment(MultipartFile file) {
        if (file == null || file.isEmpty())
            return null;

        String contentType = file.getContentType();
        if (!(contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("application/pdf"))) {
            throw new IllegalArgumentException("Only Jpg, jpeg, png or pdf files are allowed!");
        }
        File dir = new File(leaveUploadDir);
        if (!dir.exists())
            dir.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(leaveUploadDir, fileName);

        try {
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return leaveUploadDir + fileName;
    }

}
