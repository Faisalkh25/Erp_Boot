package com.erp.service;

import java.time.Period;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.dto.ProjectDetailsRequestDto;
import com.erp.model.Employee;
import com.erp.model.EmployeeSalary;
import com.erp.model.ProjectDetails;
import com.erp.repository.ClientRepository;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.EmployeeSalaryRepository;
import com.erp.repository.ProjectDetailsRepository;
import com.erp.repository.TaskPriorityRepository;
import com.erp.repository.TaskProjectRepository;
import com.erp.repository.TaskStatusRepository;
import com.erp.repository.TaskTypeRepository;

@Service
public class ProjectDetailsServiceImpl implements ProjectDetailsService {

    @Autowired
    private ProjectDetailsRepository repo;
    @Autowired
    private TaskProjectRepository projectRepo;
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private TaskTypeRepository typeRepo;
    @Autowired
    private TaskPriorityRepository priorityRepo;
    @Autowired
    private TaskStatusRepository statusRepo;
    @Autowired
    private EmployeeRepository empRepo;
    @Autowired
    private EmployeeSalaryRepository salaryRepo;

    private List<Employee> buildTeamAndCalculate(ProjectDetailsRequestDto dto, ProjectDetails project) {
        List<Employee> team = new ArrayList<>();
        double totalDailyCost = 0.0;
        double totalManPerHour = 0.0;

        int projectDays = Period.between(dto.getStartDate(), dto.getEndDate()).getDays();
        if (projectDays <= 0) {
            throw new RuntimeException("Invalid project duration.");
        }

        for (Integer empId : dto.getTeamMemberIds()) {
            Employee emp = empRepo.findById(empId)
                    .orElseThrow(() -> new RuntimeException("Employee not found: " + empId));

            EmployeeSalary salary = salaryRepo.findByEmployee_EmpId(empId);
            if (salary == null) {
                throw new RuntimeException("Salary not found for employee: " + empId);
            }

            int daysInMonth = YearMonth.now().lengthOfMonth();
            double perDay = salary.getMonthlySalary() / daysInMonth;
            double perHour = perDay / 9.0;

            totalDailyCost += perDay * projectDays;
            totalManPerHour += perHour;

            team.add(emp);
        }

        if (totalDailyCost > dto.getRate()) {
            throw new RuntimeException("Project cost exceeds rate. Please choose a different team.");
        }

        project.setTeamMembers(team);
        project.setRateCalculation(totalDailyCost);
        project.setManPerHour(totalManPerHour);

        return team;
    }

    @Override
    public String createProject(ProjectDetailsRequestDto dto) {

        ProjectDetails project = new ProjectDetails();

        project.setProjectName(projectRepo.findById(dto.getProjectId()).orElseThrow());
        project.setClient(clientRepo.findById(dto.getClientId()).orElseThrow());
        project.setProjectType(typeRepo.findById(dto.getTypeId()).orElseThrow());
        project.setProjectPriority(priorityRepo.findById(dto.getPriorityId()).orElseThrow());
        project.setProject_status(statusRepo.findById(dto.getStatusId()).orElseThrow());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setSubmissionDate(dto.getSubmissionDate());
        project.setProjectLeader(empRepo.findById(dto.getProjectLeaderId()).orElseThrow());
        project.setRate(dto.getRate());
        project.setRateType(dto.getRateType());
        project.setRemarks(dto.getRemarks());
        project.setDescription(dto.getDescription());

        buildTeamAndCalculate(dto, project);

        repo.save(project);
        return "Project saved successfully.";
    }

    /*
     * @Override
     * public String createProject(ProjectDetailsRequestDto dto) {
     * List<Employee> team = new ArrayList<>();
     * double totalDailyCost = 0.0;
     * double totalManPerHour = 0.0;
     * 
     * long projectDays = ChronoUnit.DAYS.between(dto.getStartDate(),
     * dto.getEndDate());
     * if (projectDays <= 0)
     * throw new RuntimeException("Invalid project duration");
     * 
     * int daysInMonth = YearMonth.from(dto.getStartDate()).lengthOfMonth(); // 30
     * or 31
     * 
     * for (Integer empId : dto.getTeamMemberIds()) {
     * Employee emp = empRepo.findById(empId)
     * .orElseThrow(() -> new RuntimeException("Employee not found: " + empId));
     * 
     * // EmployeeSalary salary = salaryRepo.findByEmployee_EmpId(empId)
     * // .orElseThrow(() -> new RuntimeException("Salary not found for employee: "
     * +
     * // empId));
     * 
     * EmployeeSalary salary = salaryRepo.findByEmployee_EmpId(empId);
     * if (salary == null) {
     * throw new RuntimeException("salary not found for employee : " + empId);
     * }
     * 
     * double perDay = salary.getMonthlySalary() / daysInMonth;
     * double perHour = perDay / 9.0;
     * 
     * totalDailyCost += perDay * projectDays;
     * totalManPerHour += perHour;
     * 
     * team.add(emp);
     * }
     * 
     * if (totalDailyCost > dto.getRate()) {
     * return "Project cost exceeds rate. Please revise team members.";
     * }
     * 
     * ProjectDetails project = new ProjectDetails();
     * project.setProjectName(projectRepo.findById(dto.getProjectId()).orElseThrow()
     * );
     * project.setClient(clientRepo.findById(dto.getClientId()).orElseThrow());
     * project.setProjectType(typeRepo.findById(dto.getTypeId()).orElseThrow());
     * project.setProjectPriority(priorityRepo.findById(dto.getPriorityId()).
     * orElseThrow());
     * project.setProject_status(statusRepo.findById(dto.getStatusId()).orElseThrow(
     * ));
     * project.setStartDate(dto.getStartDate());
     * project.setEndDate(dto.getEndDate());
     * project.setSubmissionDate(dto.getSubmissionDate());
     * project.setProjectLeader(empRepo.findById(dto.getProjectLeaderId()).
     * orElseThrow());
     * project.setRate(dto.getRate());
     * project.setRateType(dto.getRateType());
     * project.setTeamMembers(team);
     * project.setRemarks(dto.getRemarks());
     * project.setDescription(dto.getDescription());
     * project.setManPerHour(totalManPerHour);
     * project.setRateCalculation(totalDailyCost);
     * 
     * repo.save(project);
     * return "Project saved successfully.";
     * }
     * 
     */

    @Override
    public String updateProject(int id, ProjectDetailsRequestDto dto) {
        ProjectDetails project = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));

        project.setProjectName(projectRepo.findById(dto.getProjectId()).orElseThrow());
        project.setClient(clientRepo.findById(dto.getClientId()).orElseThrow());
        project.setProjectType(typeRepo.findById(dto.getTypeId()).orElseThrow());
        project.setProjectPriority(priorityRepo.findById(dto.getPriorityId()).orElseThrow());
        project.setProject_status(statusRepo.findById(dto.getStatusId()).orElseThrow());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setSubmissionDate(dto.getSubmissionDate());
        project.setProjectLeader(empRepo.findById(dto.getProjectLeaderId()).orElseThrow());
        project.setRate(dto.getRate());
        project.setRateType(dto.getRateType());
        project.setRemarks(dto.getRemarks());
        project.setDescription(dto.getDescription());

        buildTeamAndCalculate(dto, project);

        repo.save(project);
        return "Project updated successfully.";
    }

    @Override
    public ProjectDetails getProjectById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
    }

    @Override
    public List<ProjectDetails> getAllProjectDetails() {
        return repo.findAll();
    }

    @Override
    public String deleteProjectDetails(int id) {
        ProjectDetails project = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
        repo.delete(project);
        return "Project deleted successfully.";
    }
}
