package com.erp.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "leavetypes")
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leavetype_id")
    private int leavetype_id;
    private String leave_type;

    // @CreationTimestamp
    // @Column(name = "date_created", updatable = false)
    // private Timestamp dateCreated;

    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateCreated = LocalDateTime.now();

    public LeaveType() {
    }

    public LeaveType(int leavetype_id, String leave_type, LocalDateTime dateCreated) {
        this.leavetype_id = leavetype_id;
        this.leave_type = leave_type;
        this.dateCreated = dateCreated;
    }

    public int getLeavetype_id() {
        return leavetype_id;
    }

    public void setLeavetype_id(int leavetype_id) {
        this.leavetype_id = leavetype_id;
    }

    public String getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(String leave_type) {
        this.leave_type = leave_type;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

}
