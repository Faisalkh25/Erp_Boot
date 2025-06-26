package com.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Handlers for admin dashboard pages like, user-profile,
    // show-add-employee, show-add-department

    @GetMapping("/index")
    public String home() {
        return "dashboard/index";
    }

    @GetMapping("/user-profile")
    public String employeeProfile() {
        return "dashboard/app/user-profile";
    }

    @GetMapping("/show-add-employee")
    public String showAddEmployee() {
        return "dashboard/app/user-add";
    }

    @GetMapping("/show-add-department")
    public String showAddDepartment() {
        return "dashboard/app/department";
    }

    @GetMapping("/show-list-employee")
    public String showListEmployee() {
        return "dashboard/app/user-list";
    }

    @GetMapping("/show-leave")
    public String showLeaveType() {
        return "dashboard/app/leave-type";
    }

    @GetMapping("/show-shift")
    public String showShift() {
        return "dashboard/app/shift";
    }

    @GetMapping("/show-level")
    public String showLevel() {
        return "dashboard/app/add-level";
    }

}
