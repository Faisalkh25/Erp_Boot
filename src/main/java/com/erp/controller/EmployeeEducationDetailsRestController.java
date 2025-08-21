package com.erp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.dto.EmployeeEducationDetailsDto;
import com.erp.model.Employee;
import com.erp.model.EmployeeEducationDetails;
import com.erp.repository.EmployeeRepository;
import com.erp.service.EmployeeEducationDetailsService;

@RestController
@RequestMapping("/api/employees/education-details")
@CrossOrigin("*")
public class EmployeeEducationDetailsRestController {

    private final EmployeeEducationDetailsService detailsService;
    private final EmployeeRepository employeeRepo;

    public EmployeeEducationDetailsRestController(EmployeeEducationDetailsService detailsService,
            EmployeeRepository employeeRepo) {
        this.detailsService = detailsService;
        this.employeeRepo = employeeRepo;
    }

    @PostMapping
    public ResponseEntity<EmployeeEducationDetails> saveOrUpdate(@RequestBody EmployeeEducationDetailsDto dto) {
        if (dto.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }

        Employee employee = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + dto.getEmployeeId()));

        EmployeeEducationDetails details = detailsService.findByEmployee(employee)
                .orElse(new EmployeeEducationDetails());

        details.setQualification(dto.getQualification());
        details.setUniversity(dto.getUniversity());
        details.setRoleNumber(dto.getRoleNumber());
        details.setSubject(dto.getSubject());
        details.setPassingYear(dto.getPassingYear());
        details.setMarks(dto.getMarks());
        details.setEmployee(employee);

        EmployeeEducationDetails savedDetails = detailsService.saveOrUpdate(details);
        return new ResponseEntity<>(savedDetails, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<EmployeeEducationDetails> getByEmployee(@PathVariable Integer employeeId) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));

        EmployeeEducationDetails details = detailsService.findByEmployee(employee)
                .orElse(null);

        if (details == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(details, HttpStatus.OK);
    }

}
