package com.erp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.model.Employee;
import com.erp.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepo;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // @Override
    // public Employee createEmployee(Employee employee, MultipartFile file) throws
    // IOException {

    // if (file != null && !file.isEmpty()) {
    // String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    // Path filePath = Paths.get(uploadDir, fileName);
    // Files.copy(file.getInputStream(), filePath,
    // StandardCopyOption.REPLACE_EXISTING);
    // employee.setProfile_picture(fileName);
    // }
    // return employeeRepo.save(employee);

    // }

    @Override
    public Employee createEmployee(Employee employee, MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            employee.setProfile_picture(fileName);
        }

        return employeeRepo.save(employee);
    }

    @Override
    public List<Employee> showAllEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public Employee getSingleEmployee(int id) {
        return employeeRepo.findById(id).orElseThrow(() -> new RuntimeException("employee id not found : " + id));
    }

    // @Override
    // public Employee updateEmployee(Employee employee, int id, MultipartFile file)
    // {
    // Employee existingEmployee = getSingleEmployee(id);

    // existingEmployee.setEmp_code(employee.getEmp_code());
    // existingEmployee.setFirst_name(employee.getFirst_name());
    // existingEmployee.setLast_name(employee.getLast_name());
    // existingEmployee.setDateOfBirth(employee.getDateOfBirth());
    // existingEmployee.setEmail(employee.getEmail());
    // existingEmployee.setPersonal_email(employee.getPersonal_email());
    // existingEmployee.setAddress(employee.getAddress());
    // existingEmployee.setContact(employee.getContact());
    // existingEmployee.setJoining_date(employee.getJoining_date());
    // existingEmployee.setGender(employee.getGender());
    // existingEmployee.setDepartment(employee.getDepartment());
    // existingEmployee.setEmp_level(employee.getEmp_level());
    // existingEmployee.setCompany(employee.getCompany());
    // existingEmployee.setShift(employee.getShift());
    // existingEmployee.setEmployee_status(employee.getEmployee_status());
    // existingEmployee.setJoining_status(employee.getJoining_status());
    // existingEmployee.setWorking_status(employee.getWorking_status());
    // existingEmployee.setRole(employee.getRole());
    // existingEmployee.setReporting_manager1(employee.getReporting_manager1());
    // existingEmployee.setReporting_manager2(employee.getReporting_manager2());

    // if (file != null && !file.isEmpty()) {

    // String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    // Path filePath = Paths.get(uploadDir, fileName);
    // try {
    // Files.copy(file.getInputStream(), filePath,
    // StandardCopyOption.REPLACE_EXISTING);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // existingEmployee.setProfile_picture(fileName);
    // }
    // return employeeRepo.save(existingEmployee);

    // }

    @Override
    public Employee updateEmployee(Employee employee, int id, MultipartFile file) {
        Employee existingEmployee = getSingleEmployee(id);

        existingEmployee.setEmp_code(employee.getEmp_code());
        existingEmployee.setFirst_name(employee.getFirst_name());
        existingEmployee.setLast_name(employee.getLast_name());
        existingEmployee.setDateOfBirth(employee.getDateOfBirth());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setPersonal_email(employee.getPersonal_email());
        existingEmployee.setAddress(employee.getAddress());
        existingEmployee.setContact(employee.getContact());
        existingEmployee.setJoining_date(employee.getJoining_date());
        existingEmployee.setGender(employee.getGender());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setEmp_level(employee.getEmp_level());
        existingEmployee.setCompany(employee.getCompany());
        existingEmployee.setShift(employee.getShift());
        existingEmployee.setEmployee_status(employee.getEmployee_status());
        existingEmployee.setJoining_status(employee.getJoining_status());
        existingEmployee.setWorking_status(employee.getWorking_status());
        existingEmployee.setRole(employee.getRole());
        existingEmployee.setReporting_manager1(employee.getReporting_manager1());
        existingEmployee.setReporting_manager2(employee.getReporting_manager2());

        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir); // "uploads" folder
            Path filePath = uploadPath.resolve(fileName);

            try {
                // Create the upload directory if it does not exist
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                existingEmployee.setProfile_picture(fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return employeeRepo.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(int id) {
        employeeRepo.deleteById(id);
    }

}
