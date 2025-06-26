package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
