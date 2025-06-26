package com.erp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.erp.model.Employee;

public interface EmployeeService {

    public Employee createEmployee(Employee employee, MultipartFile file) throws IOException;

    public List<Employee> showAllEmployees();

    public Employee getSingleEmployee(int id);

    public Employee updateEmployee(Employee employee, int id, MultipartFile file);

    public void deleteEmployee(int id);
}
