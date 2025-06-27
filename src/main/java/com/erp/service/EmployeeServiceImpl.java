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

import com.erp.model.Department;
import com.erp.model.Employee;
import com.erp.model.Level;
import com.erp.model.Shift;
import com.erp.repository.DepartmentRepository;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.LevelRepository;
import com.erp.repository.ShiftRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private ShiftRepository shiftRepo;

    @Autowired
    private LevelRepository levelRepo;

    @Value("${file.upload-dir}")
    private String uploadDir;

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

        // getDepartment foreign objects
        if (employee.getDepartment() != null && employee.getDepartment().getDept_id() != 0) {
            Department dept = departmentRepo.findById(employee.getDepartment().getDept_id())
                    .orElseThrow(() -> new RuntimeException("Invalid department Id"));
            employee.setDepartment(dept);
        }

        // get Level foreign objects
        if (employee.getEmp_level() != null && employee.getEmp_level().getLevel_id() != 0) {
            Level level = levelRepo.findById(employee.getEmp_level().getLevel_id())
                    .orElseThrow(() -> new RuntimeException("Invalid level Id"));
            employee.setEmp_level(level);
        }

        // get Shift foreign objects
        if (employee.getShift() != null && employee.getShift().getShift_id() != 0) {
            Shift shift = shiftRepo.findById(employee.getShift().getShift_id())
                    .orElseThrow(() -> new RuntimeException("invalid shift id"));
            employee.setShift(shift);
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
        existingEmployee.setPassword(employee.getPassword());

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

        // getDepartment foreign objects
        if (employee.getDepartment() != null && employee.getDepartment().getDept_id() != 0) {
            Department dept = departmentRepo.findById(employee.getDepartment().getDept_id())
                    .orElseThrow(() -> new RuntimeException("Invalid department Id"));
            existingEmployee.setDepartment(dept);
        }

        // get Level foreign objects
        if (employee.getEmp_level() != null && employee.getEmp_level().getLevel_id() != 0) {
            Level level = levelRepo.findById(employee.getEmp_level().getLevel_id())
                    .orElseThrow(() -> new RuntimeException("Invalid level Id"));
            existingEmployee.setEmp_level(level);
        }

        // get Shift foreign objects
        if (employee.getShift() != null && employee.getShift().getShift_id() != 0) {
            Shift shift = shiftRepo.findById(employee.getShift().getShift_id())
                    .orElseThrow(() -> new RuntimeException("invalid shift id"));
            existingEmployee.setShift(shift);
        }

        return employeeRepo.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(int id) {
        employeeRepo.deleteById(id);
    }

}
