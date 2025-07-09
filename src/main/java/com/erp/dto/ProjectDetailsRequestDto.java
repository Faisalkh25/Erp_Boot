package com.erp.dto;

import java.time.LocalDate;
import java.util.List;

public class ProjectDetailsRequestDto {

    private int projectId;
    private int clientId;
    private int typeId;
    private int priorityId;
    private int statusId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate submissionDate;

    private int projectLeaderId;
    private Double rate;
    private String rateType;

    private List<Integer> teamMemberIds;

    private String remarks;
    private String description;

    public ProjectDetailsRequestDto() {
    }

    public ProjectDetailsRequestDto(int projectId, int clientId, int typeId, int priorityId, int statusId,
            LocalDate startDate, LocalDate endDate, LocalDate submissionDate, int projectLeaderId, Double rate,
            String rateType, List<Integer> teamMemberIds, String remarks, String description) {
        this.projectId = projectId;
        this.clientId = clientId;
        this.typeId = typeId;
        this.priorityId = priorityId;
        this.statusId = statusId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.submissionDate = submissionDate;
        this.projectLeaderId = projectLeaderId;
        this.rate = rate;
        this.rateType = rateType;
        this.teamMemberIds = teamMemberIds;
        this.remarks = remarks;
        this.description = description;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public int getProjectLeaderId() {
        return projectLeaderId;
    }

    public void setProjectLeaderId(int projectLeaderId) {
        this.projectLeaderId = projectLeaderId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getTeamMemberIds() {
        return teamMemberIds;
    }

    public void setTeamMemberIds(List<Integer> teamMemberIds) {
        this.teamMemberIds = teamMemberIds;
    }

}
