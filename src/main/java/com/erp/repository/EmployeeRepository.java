package com.erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.erp.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByDepartment_DeptId(int deptId);

    @Query("SELECT MAX(e.emp_code) from Employee e")
    public Integer findMaxEmpCode();
}
