package com.erp.service;

import java.util.List;

import com.erp.model.EmployeeSalary;

public interface EmployeeSalaryService {

    public EmployeeSalary saveEmployeeSalary(EmployeeSalary salary);

    public List<EmployeeSalary> getAllSalary();

    public EmployeeSalary getSingleSalary(int id);

    public EmployeeSalary updateSalary(int id, EmployeeSalary salary);

    public void deleteSalary(int id);

    // get salary by employee Id
    public EmployeeSalary getSalaryByEmployeeId(int empId);

}
