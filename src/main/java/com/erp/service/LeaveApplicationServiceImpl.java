package com.erp.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
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
import com.erp.util.LeaveCalculator;

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

        System.out.println("Applying leave for empId=" + dto.getEmployeeId()
                + ", leaveTypeId=" + dto.getLeaveTypeId()
                + ", applyToId=" + dto.getApplyToId());

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

            dto.setLeaveApplicationId(leave.getLeaveApplicationId());

            // employee details
            dto.setEmployeeId(leave.getEmployee().getEmpId());
            dto.setEmployeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName());
            dto.setEmployeeCode(leave.getEmployee().getEmpCode());

            // leave type
            dto.setLeaveTypeId(leave.getLeaveType().getLeavetype_id());
            dto.setLeaveTypeName(leave.getLeaveType().getLeave_type());

            // dates
            dto.setSessionFrom(leave.getSessionFrom());
            dto.setSessionTo(leave.getSessionTo());
            dto.setFromDate(leave.getFromDate());
            dto.setToDate(leave.getToDate());
            dto.setAppliedDate(leave.getDateCreated());

            // qty
            int fromSession = mapSession(leave.getSessionFrom());
            int toSession = mapSession(leave.getSessionTo());

            double quantity = LeaveCalculator.calculateLeaveQuantity(leave.getFromDate(), leave.getToDate(),
                    fromSession, toSession);

            dto.setLeaveQuantity(quantity);

            dto.setContactDetails(leave.getContactDetails());

            // apply to
            dto.setApplyToId(leave.getApplyTo().getEmpId());
            dto.setApplyToName(leave.getApplyTo().getFirstName() + " " + leave.getApplyTo().getLastName());

            dto.setReason(leave.getReason());

            // cc employees
            dto.setCcEmployeeIds(
                    leave.getCcEmployees().stream().map(e -> e.getEmpId()).toList());

            dto.setCcEmployeeNames(
                    leave.getCcEmployees().stream()
                            .map(e -> e.getFirstName() + " " + e.getLastName())
                            .toList());

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

    @Override
    public boolean updateLeaveStatusByRM(int leaveId, String status, int rmEmpId) {
        LeaveApplication leave = leaveRepo.findById(leaveId).orElse(null);
        if (leave == null)
            return false;

        // ensuring the loggedin employee is apply to of the leave
        if (leave.getApplyTo() == null || leave.getApplyTo().getEmpId() != rmEmpId) {
            return false;
        }

        leave.setStatus(status.toUpperCase());
        leaveRepo.save(leave);
        return true;
    }

    // RM DTO mapper
    @Override
    public List<LeaveApplicationDto> getLeavesForRM(int rmEmpId) {
        List<LeaveApplication> leaves = leaveRepo.findByApplyToEmpId(rmEmpId);

        return leaves.stream().map(leave -> {
            LeaveApplicationDto dto = new LeaveApplicationDto();

            dto.setLeaveApplicationId(leave.getLeaveApplicationId());

            // employee details
            dto.setEmployeeId(leave.getEmployee().getEmpId());
            dto.setEmployeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName());
            dto.setEmployeeCode(leave.getEmployee().getEmpCode());

            // leave type
            dto.setLeaveTypeId(leave.getLeaveType().getLeavetype_id());
            dto.setLeaveTypeName(leave.getLeaveType().getLeave_type());

            // dates
            dto.setSessionFrom(leave.getSessionFrom());
            dto.setSessionTo(leave.getSessionTo());
            dto.setFromDate(leave.getFromDate());
            dto.setToDate(leave.getToDate());
            dto.setAppliedDate(leave.getDateCreated());

            // qty
            int fromSession = mapSession(leave.getSessionFrom());
            int toSession = mapSession(leave.getSessionTo());

            double quantity = LeaveCalculator.calculateLeaveQuantity(leave.getFromDate(), leave.getToDate(),
                    fromSession, toSession);

            dto.setLeaveQuantity(quantity);

            dto.setContactDetails(leave.getContactDetails());

            // apply to
            dto.setApplyToId(leave.getApplyTo().getEmpId());
            dto.setApplyToName(leave.getApplyTo().getFirstName() + " " + leave.getApplyTo().getLastName());

            dto.setReason(leave.getReason());

            // cc employees
            dto.setCcEmployeeIds(
                    leave.getCcEmployees().stream().map(e -> e.getEmpId()).toList());

            dto.setCcEmployeeNames(
                    leave.getCcEmployees().stream()
                            .map(e -> e.getFirstName() + " " + e.getLastName())
                            .toList());

            dto.setStatus(leave.getStatus());

            return dto;
        }).toList();
    }

    // helper method for converting string session from , session To
    private int mapSession(String session) {
        if (session == null)
            return 1;
        return switch (session.trim().toLowerCase()) {
            case "session 1", "1", "one" -> 1;
            case "session 2", "2", "two" -> 2;
            case "session 3", "3", "three" -> 3;
            case "session 4", "4", "four" -> 4;
            default -> 1;
        };
    }

}
