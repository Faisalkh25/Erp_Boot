package com.erp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int emp_id;
    private int emp_code;

    @Column(nullable = false)
    private String first_name;
    @Column(nullable = false)
    private String last_name;
    private LocalDate dateOfBirth;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String personal_email;

    private String address;
    @Column(nullable = false)
    private String contact;

    private LocalDate joining_date;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private String gender;

    @ManyToOne
    @JoinColumn(name = "emp_level_id")
    private Level emp_level;

    private String company;

    @ManyToOne
    @JoinColumn(name = "emp_shift_id")
    private Shift shift;

    private String employee_status;
    private String joining_status;
    private String working_status;

    @ManyToOne
    @JoinColumn(name = "reporting_manager1_id")
    private ReportingManager reporting_manager1;

    @ManyToOne
    @JoinColumn(name = "reporting_manager2_id")
    private ReportingManager reporting_manager2;

    @ManyToOne
    @JoinColumn(name = "emp_role")
    private Role role;

    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime date_created = LocalDateTime.now();

    private LocalDateTime updated_at = LocalDateTime.now();

    @Column(nullable = false)
    private String password;

    private String profile_picture;

    @PreUpdate
    public void setLastUpdate() {
        this.updated_at = LocalDateTime.now();
    }

    public Employee() {
    }

    public Employee(int emp_id, int emp_code, String first_name, String last_name, LocalDate dateOfBirth, String email,
            String personal_email, String address, String contact, LocalDate joining_date, Department department,
            String gender, Level emp_level, String company, Shift shift, String employee_status, String joining_status,
            String working_status, ReportingManager reporting_manager1, ReportingManager reporting_manager2, Role role,
            LocalDateTime date_created, LocalDateTime updated_at, String password, String profile_picture) {
        this.emp_id = emp_id;
        this.emp_code = emp_code;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.personal_email = personal_email;
        this.address = address;
        this.contact = contact;
        this.joining_date = joining_date;
        this.department = department;
        this.gender = gender;
        this.emp_level = emp_level;
        this.company = company;
        this.shift = shift;
        this.employee_status = employee_status;
        this.joining_status = joining_status;
        this.working_status = working_status;
        this.reporting_manager1 = reporting_manager1;
        this.reporting_manager2 = reporting_manager2;
        this.role = role;
        this.date_created = date_created;
        this.updated_at = updated_at;
        this.password = password;
        this.profile_picture = profile_picture;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public int getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(int emp_code) {
        this.emp_code = emp_code;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonal_email() {
        return personal_email;
    }

    public void setPersonal_email(String personal_email) {
        this.personal_email = personal_email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDate getJoining_date() {
        return joining_date;
    }

    public void setJoining_date(LocalDate joining_date) {
        this.joining_date = joining_date;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Level getEmp_level() {
        return emp_level;
    }

    public void setEmp_level(Level emp_level) {
        this.emp_level = emp_level;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public String getEmployee_status() {
        return employee_status;
    }

    public void setEmployee_status(String employee_status) {
        this.employee_status = employee_status;
    }

    public String getJoining_status() {
        return joining_status;
    }

    public void setJoining_status(String joining_status) {
        this.joining_status = joining_status;
    }

    public String getWorking_status() {
        return working_status;
    }

    public void setWorking_status(String working_status) {
        this.working_status = working_status;
    }

    public ReportingManager getReporting_manager1() {
        return reporting_manager1;
    }

    public void setReporting_manager1(ReportingManager reporting_manager1) {
        this.reporting_manager1 = reporting_manager1;
    }

    public ReportingManager getReporting_manager2() {
        return reporting_manager2;
    }

    public void setReporting_manager2(ReportingManager reporting_manager2) {
        this.reporting_manager2 = reporting_manager2;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getDate_created() {
        return date_created;
    }

    public void setDate_created(LocalDateTime date_created) {
        this.date_created = date_created;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

}
