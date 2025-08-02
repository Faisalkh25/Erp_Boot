package com.erp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.erp.dto.EmployeeDto;
import com.erp.model.Employee;

public interface EmployeeService {

    // public Employee createEmployee(Employee employee, MultipartFile file) throws
    // IOException;
    public Employee createEmployee(EmployeeDto dto, MultipartFile file) throws IOException;

    // public List<Employee> showAllEmployees();
    public List<EmployeeDto> getAllEmployeesDto();

    public Employee getSingleEmployee(int id);

    // public Employee updateEmployee(Employee employee, int id, MultipartFile
    // file);
    public Employee updateEmployee(EmployeeDto dto, int id, MultipartFile file) throws IOException;

    public void deleteEmployee(int id);

    List<Employee> getEmployeesByDepartmentId(int deptId);

    // method for get Employee Code

    public Employee findByEmpCode(int empCode);

}
