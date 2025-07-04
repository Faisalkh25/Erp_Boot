package com.erp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.dto.EmployeeDto;
import com.erp.model.Employee;
import com.erp.repository.DepartmentRepository;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.LevelRepository;
import com.erp.repository.ReportingManagerRepository;
import com.erp.repository.RoleRepository;
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

    @Autowired
    private ReportingManagerRepository rmRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public Employee createEmployee(EmployeeDto dto, MultipartFile file) throws IOException {

        Employee employee = mapDtoToEmployee(dto, new Employee());

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
        // System.out.println("DTO Department ID: " + dto.getDepartmentId());
        // System.out.println("Resolved Department: " +
        // departmentRepo.findById(dto.getDepartmentId()).orElse(null));

        return employeeRepo.save(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployeesDto() {
        return employeeRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Employee getSingleEmployee(int id) {
        return employeeRepo.findById(id).orElseThrow(() -> new RuntimeException("employee id not found : " + id));
    }

    @Override
    public Employee updateEmployee(EmployeeDto dto, int id, MultipartFile file) throws IOException {
        Employee existingEmployee = getSingleEmployee(id);

        Employee updated = mapDtoToEmployee(dto, existingEmployee);

        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir); // "uploads" folder
            Path filePath = uploadPath.resolve(fileName);

            // Create the upload directory if it does not exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            updated.setProfile_picture(fileName);

        }

        return employeeRepo.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(int id) {
        employeeRepo.deleteById(id);
    }

    // method for get employees by departmentId
    @Override
    public List<Employee> getEmployeesByDepartmentId(int deptId) {
        List<Employee> employees = employeeRepo.findByDepartment_DeptId(deptId);
        return employees;

    }

    // mapDto to employee
    private Employee mapDtoToEmployee(EmployeeDto dto, Employee employee) {
        employee.setEmp_code(dto.getEmp_code());
        employee.setFirst_name(dto.getFirst_name());
        employee.setLast_name(dto.getLast_name());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setEmail(dto.getEmail());
        employee.setPersonal_email(dto.getPersonal_email());
        employee.setAddress(dto.getAddress());
        employee.setContact(dto.getContact());
        employee.setJoining_date(dto.getJoining_date());
        employee.setGender(dto.getGender());
        // employee.setDepartment(departmentRepo.findById(dto.getDepartmentId()).orElse(null));
        if (dto.getDepartmentId() > 0) {
            employee.setDepartment(departmentRepo.findById(dto.getDepartmentId()).orElse(null));
        }
        // employee.setEmp_level(levelRepo.findById(dto.getLevelId()).orElse(null));
        if (dto.getLevelId() > 0) {
            employee.setEmp_level(levelRepo.findById(dto.getLevelId()).orElse(null));
        }
        employee.setCompany(dto.getCompany());
        // employee.setShift(shiftRepo.findById(dto.getShiftId()).orElse(null));
        if (dto.getShiftId() > 0) {
            employee.setShift(shiftRepo.findById(dto.getShiftId()).orElse(null));
        }
        employee.setEmployee_status(dto.getEmployee_status());
        employee.setJoining_status(dto.getJoining_status());
        employee.setWorking_status(dto.getWorking_status());
        // employee.setRole(roleRepo.findById(dto.getRoleId()).orElse(null));
        if (dto.getRoleId() > 0) {
            employee.setRole(roleRepo.findById(dto.getRoleId()).orElse(null));
        }
        employee.setPassword(dto.getPassword());

        if (dto.getReportingManager1Id() != null && dto.getReportingManager1Id() > 0)
            employee.setReporting_manager1(rmRepo.findById(dto.getReportingManager1Id()).orElse(null));

        if (dto.getReportingManager2Id() != null && dto.getReportingManager2Id() > 0)
            employee.setReporting_manager2(rmRepo.findById(dto.getReportingManager2Id()).orElse(null));

        return employee;
    }

    // employee convertToDto method that will return trimmed data for the frontend
    // like
    // department name, level name, shift name, role name, reporting manager 1 name,
    // reporting manager 2 name

    private EmployeeDto convertToDto(Employee emp) {

        EmployeeDto dto = new EmployeeDto();

        dto.setEmp_id(emp.getEmp_id());
        dto.setEmp_code(emp.getEmp_code());
        dto.setFirst_name(emp.getFirst_name());
        dto.setLast_name(emp.getLast_name());
        dto.setDateOfBirth(emp.getDateOfBirth());
        dto.setGender(emp.getGender());
        dto.setEmail(emp.getEmail());
        dto.setPersonal_email(emp.getPersonal_email());
        dto.setAddress(emp.getAddress());
        dto.setContact(emp.getContact());
        dto.setJoining_date(emp.getJoining_date());
        dto.setCompany(emp.getCompany());
        dto.setEmployee_status(emp.getEmployee_status());
        dto.setJoining_status(emp.getJoining_status());
        dto.setWorking_status(emp.getWorking_status());
        dto.setPassword(emp.getPassword());
        dto.setProfile_picture(emp.getProfile_picture());

        if (emp.getDepartment() != null) {
            dto.setDepartmentId(emp.getDepartment().getDeptId());
            dto.setDepartmentName(emp.getDepartment().getDept_name());
            System.out.println("Dept: " + emp.getDepartment());
            System.out.println("Dept Name: " + emp.getDepartment().getDept_name());
        }

        if (emp.getEmp_level() != null) {
            dto.setLevelId(emp.getEmp_level().getLevel_id());
            dto.setLevelName(emp.getEmp_level().getLevel());
        }

        if (emp.getShift() != null) {
            dto.setShiftId(emp.getShift().getShift_id());
            dto.setShiftName(emp.getShift().getShift_name());
        }

        if (emp.getRole() != null) {
            dto.setRoleId(emp.getRole().getRole_id());
            dto.setRoleName(emp.getRole().getRole_name());
        }

        if (emp.getReporting_manager1() != null) {
            dto.setReportingManager1Id(emp.getReporting_manager1().getRm_id());
            dto.setReportingManager1Name(emp.getReporting_manager1().getRm_name1());
        }

        if (emp.getReporting_manager2() != null) {
            dto.setReportingManager2Id(emp.getReporting_manager2().getRm_id());
            dto.setReportingManager2Name(emp.getReporting_manager2().getRm_name2());
        }

        return dto;
    }

}
