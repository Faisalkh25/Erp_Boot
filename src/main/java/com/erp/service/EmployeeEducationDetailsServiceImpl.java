package com.erp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.erp.model.Employee;
import com.erp.model.EmployeeEducationDetails;
import com.erp.repository.EmployeeEducationDetailsRepository;

@Service
public class EmployeeEducationDetailsServiceImpl implements EmployeeEducationDetailsService {

    private final EmployeeEducationDetailsRepository repository;

    public EmployeeEducationDetailsServiceImpl(EmployeeEducationDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    public EmployeeEducationDetails saveOrUpdate(EmployeeEducationDetails details) {
        return repository.save(details);
    }

    @Override
    public Optional<EmployeeEducationDetails> findByEmployee(Employee employee) {
        return repository.findByEmployee(employee);
    }

}
