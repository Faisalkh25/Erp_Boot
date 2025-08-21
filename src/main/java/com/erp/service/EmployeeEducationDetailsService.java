package com.erp.service;

import java.util.Optional;

import com.erp.model.Employee;
import com.erp.model.EmployeeEducationDetails;

public interface EmployeeEducationDetailsService {

    EmployeeEducationDetails saveOrUpdate(EmployeeEducationDetails details);

    Optional<EmployeeEducationDetails> findByEmployee(Employee employee);

}
