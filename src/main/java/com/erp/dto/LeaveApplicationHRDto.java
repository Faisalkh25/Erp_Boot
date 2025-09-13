package com.erp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveApplicationHRDto {

    private int leaveApplicationId;
    private int employeeId;
    private String employeeName;
    private int employeeCode;
    private int leaveTypeId;
    private String leaveTypeName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String sessionFrom;
    private String sessionTo;
    private String contactDetails;
    private String reason;
    private String status;
    private LocalDateTime dateCreated;

    private int applyToId;
    private String applyToName;
    private long qty;
    private String attachmentPath;

    public LeaveApplicationHRDto() {
    }

    public LeaveApplicationHRDto(int leaveApplicationId, int employeeId, String employeeName, int employeeCode,
            int leaveTypeId, String leaveTypeName, LocalDate fromDate, LocalDate toDate, String sessionFrom,
            String sessionTo, String contactDetails, String reason, String status, LocalDateTime dateCreated,
            int applyToId, String applyToName, long qty, String attachmentPath) {
        this.leaveApplicationId = leaveApplicationId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeCode = employeeCode;
        this.leaveTypeId = leaveTypeId;
        this.leaveTypeName = leaveTypeName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.sessionFrom = sessionFrom;
        this.sessionTo = sessionTo;
        this.contactDetails = contactDetails;
        this.reason = reason;
        this.status = status;
        this.dateCreated = dateCreated;
        this.applyToId = applyToId;
        this.applyToName = applyToName;
        this.qty = qty;
        this.attachmentPath = attachmentPath;
    }

    public int getLeaveApplicationId() {
        return leaveApplicationId;
    }

    public void setLeaveApplicationId(int leaveApplicationId) {
        this.leaveApplicationId = leaveApplicationId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
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

    public int getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(int leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
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

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getApplyToId() {
        return applyToId;
    }

    public void setApplyToId(int applyToId) {
        this.applyToId = applyToId;
    }

    public String getApplyToName() {
        return applyToName;
    }

    public void setApplyToName(String applyToName) {
        this.applyToName = applyToName;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }

}
