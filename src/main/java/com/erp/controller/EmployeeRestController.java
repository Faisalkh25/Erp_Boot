package com.erp.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erp.dto.EmployeeDto;
import com.erp.model.Employee;
import com.erp.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin("*")
public class EmployeeRestController {

    @Autowired
    private EmployeeService employeeService;

    private final ObjectMapper mapper;

    public EmployeeRestController() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // handler for saving employee
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Employee> createEmployeeWithImage(
            @RequestPart("employee") String employeeJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        // ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.registerModule(new JavaTimeModule());
        // objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        EmployeeDto employeedto = mapper.readValue(employeeJson, EmployeeDto.class);

        // Employee employee = objectMapper.readValue(employeeJson, Employee.class);

        Employee savedEmployee = employeeService.createEmployee(employeedto, image);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    // handler for getAllEmployees

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> showAllEmployees() {
        List<EmployeeDto> allEmployees = employeeService.getAllEmployeesDto();

        return new ResponseEntity<>(allEmployees, HttpStatus.OK);
    }

    // handler for single employee

    @GetMapping("/{id}")
    public ResponseEntity<Employee> showSingleEmployee(@PathVariable int id) {
        Employee singleEmployee = employeeService.getSingleEmployee(id);
        return new ResponseEntity<>(singleEmployee, HttpStatus.OK);
    }

    // handler for delete Employee

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>("employee deleted successfully.", HttpStatus.GONE);
    }

    // handler for updating employee
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable int id,
            @RequestPart("employee") String employeeJson,
            @RequestPart(value = "image", required = false) MultipartFile image)
            throws IOException {

        // ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.registerModule(new JavaTimeModule());
        // objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        EmployeeDto employeedto = mapper.readValue(employeeJson, EmployeeDto.class);

        // Employee employee = objectMapper.readValue(employeeJson, Employee.class);

        Employee updated = employeeService.updateEmployee(employeedto, id, image);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // handler for getting all employees by departments like if java dept has 2
    // employees then it will show only those two employees

    @GetMapping("/by-department/{id}")
    public ResponseEntity<List<Employee>> showAllEmployeesByDepartmentId(@PathVariable int id) {
        List<Employee> empByDept = employeeService.getEmployeesByDepartmentId(id);
        return new ResponseEntity<>(empByDept, HttpStatus.OK);
    }

    // handler for getEmployee from employee code

    @GetMapping("/by-empCode/{empCode}")
    public ResponseEntity<Employee> getEmployeeByEmpCode(@PathVariable int empCode) {

        Employee emp = employeeService.findByEmpCode(empCode);
        return new ResponseEntity<>(emp, HttpStatus.OK);

    }

}
