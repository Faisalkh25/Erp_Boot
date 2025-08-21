package com.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.Employee;
import com.erp.model.EmployeeEducationDetails;

public interface EmployeeEducationDetailsRepository extends JpaRepository<EmployeeEducationDetails, Integer> {

    Optional<EmployeeEducationDetails> findByEmployee(Employee employee);

}
