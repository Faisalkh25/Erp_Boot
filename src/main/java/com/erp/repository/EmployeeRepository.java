package com.erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.erp.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByDepartment_DeptId(int deptId);

    @Query("SELECT MAX(e.empCode) from Employee e")
    public Integer findMaxEmpCode();

    public Employee findByEmpCode(int empCode);

    // method to show upcoming birthdays
    @Query("SELECT e FROM Employee e WHERE MONTH(e.dateOfBirth) = :month")
    List<Employee> findAllWithBirthdayInMonth(@Param("month") int month);

}
