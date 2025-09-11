package com.erp.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LeaveApplicationDto {

    private int employeeId;
    private String employeeName;
    private int employeeCode;
    private int leaveTypeId;
    private String leaveTypeName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String sessionFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String sessionTo;

    private LocalDate fromDate;
    private LocalDate toDate;
    private String contactDetails;
    private int applyToId;
    private List<Integer> ccEmployeeIds;
    private String reason;

    private String status;

    public LeaveApplicationDto() {
    }

    public LeaveApplicationDto(int employeeId, String employeeName, int employeeCode, int leaveTypeId,
            String leaveTypeName, String sessionFrom, String sessionTo, LocalDate fromDate, LocalDate toDate,
            String contactDetails, int applyToId, List<Integer> ccEmployeeIds, String reason, String status) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeCode = employeeCode;
        this.leaveTypeId = leaveTypeId;
        this.leaveTypeName = leaveTypeName;
        this.sessionFrom = sessionFrom;
        this.sessionTo = sessionTo;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.contactDetails = contactDetails;
        this.applyToId = applyToId;
        this.ccEmployeeIds = ccEmployeeIds;
        this.reason = reason;
        this.status = status;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(int leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getSessionFrom() {
        return sessionFrom;
    }

    public void setSessionFrom(String sessionFrom) {
        this.sessionFrom = sessionFrom;
    }

    public String getSessionTo() {
        return sessionTo;
    }

    public void setSessionTo(String sessionTo) {
        this.sessionTo = sessionTo;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public int getApplyToId() {
        return applyToId;
    }

    public void setApplyToId(int applyToId) {
        this.applyToId = applyToId;
    }

    public List<Integer> getCcEmployeeIds() {
        return ccEmployeeIds;
    }

    public void setCcEmployeeIds(List<Integer> ccEmployeeIds) {
        this.ccEmployeeIds = ccEmployeeIds;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(int employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
