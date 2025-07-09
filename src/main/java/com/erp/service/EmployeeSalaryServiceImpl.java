package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.EmployeeSalary;
import com.erp.repository.EmployeeSalaryRepository;

@Service
public class EmployeeSalaryServiceImpl implements EmployeeSalaryService {

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepo;

    @Override
    public EmployeeSalary saveEmployeeSalary(EmployeeSalary salary) {
        return employeeSalaryRepo.save(salary);
    }

    @Override
    public List<EmployeeSalary> getAllSalary() {
        List<EmployeeSalary> allSalaries = employeeSalaryRepo.findAll();
        return allSalaries;
    }

    @Override
    public EmployeeSalary getSingleSalary(int id) {
        EmployeeSalary salary = employeeSalaryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("salary id not found :" + id));
        return salary;
    }

    @Override
    public EmployeeSalary updateSalary(int id, EmployeeSalary salary) {
        EmployeeSalary existingSalary = employeeSalaryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("salary id not found :" + id));

        existingSalary.setMonthlySalary(salary.getMonthlySalary());
        existingSalary.setEmployee(salary.getEmployee());

        EmployeeSalary updatedSalary = employeeSalaryRepo.save(existingSalary);
        return updatedSalary;
    }

    @Override
    public void deleteSalary(int id) {
        employeeSalaryRepo.deleteById(id);
    }

    @Override
    public EmployeeSalary getSalaryByEmployeeId(int empId) {
        return employeeSalaryRepo.findByEmployee_EmpId(empId);
    }

}
