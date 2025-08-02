package com.erp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.erp.dto.TaskRequestDto;
import com.erp.model.AddTask;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.TaskPriorityRepository;
import com.erp.repository.TaskProjectRepository;
import com.erp.repository.TaskRepository;
import com.erp.repository.TaskStatusRepository;
import com.erp.repository.TaskSubjectRepository;
import com.erp.repository.TaskTypeRepository;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepo;
    private TaskTypeRepository taskTypeRepo;
    private TaskPriorityRepository taskPriorityRepo;
    private TaskStatusRepository taskStatusRepo;
    private TaskSubjectRepository taskSubjectRepo;
    private TaskProjectRepository taskProjectRepo;
    private EmployeeRepository employeeRepo;

    public TaskServiceImpl(TaskRepository taskRepo, TaskTypeRepository taskTypeRepo,
            TaskPriorityRepository taskPriorityRepo, TaskStatusRepository taskStatusRepo,
            TaskSubjectRepository taskSubjectRepo, TaskProjectRepository taskProjectRepo,
            EmployeeRepository employeeRepo) {
        this.taskRepo = taskRepo;
        this.taskTypeRepo = taskTypeRepo;
        this.taskPriorityRepo = taskPriorityRepo;
        this.taskStatusRepo = taskStatusRepo;
        this.taskSubjectRepo = taskSubjectRepo;
        this.taskProjectRepo = taskProjectRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public AddTask createTask(TaskRequestDto dto) {

        AddTask task = new AddTask();

        task.setTaskType(taskTypeRepo.findById(dto.getTypeId()).orElseThrow());
        task.setTaskPriority(taskPriorityRepo.findById(dto.getPriorityId()).orElseThrow());
        task.setTaskStatus(taskStatusRepo.findById(dto.getStatusId()).orElseThrow());
        task.setTaskSubject(taskSubjectRepo.findById(dto.getSubjectId()).orElseThrow());
        task.setTaskProject(taskProjectRepo.findById(dto.getProjectId()).orElseThrow());
        task.setEmployee(employeeRepo.findById(dto.getEmployeeId()).orElseThrow());

        task.setTaskQuantity(dto.getTaskQuantity());
        task.setTaskDescription(dto.getTaskDescription());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());

        AddTask newTask = taskRepo.save(task);
        return newTask;
    }

    @Override
    public List<AddTask> getAllTasks() {

        return taskRepo.findAll();
    }

    @Override
    public AddTask updateTask(int id, TaskRequestDto dto) {

        taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found with Id: " + id));

        AddTask updatedTask = mapDtoToEntity(dto);

        updatedTask.setTaskId(id);
        return taskRepo.save(updatedTask);

    }

    @Override
    public void deleteTask(int id) {
        AddTask task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("task not found with id: " + id));

        taskRepo.delete(task);

    }

    @Override
    public AddTask getTaskById(int id) {
        AddTask task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("task not found with Id: " + id));
        return task;
    }

    private AddTask mapDtoToEntity(TaskRequestDto dto) {
        AddTask task = new AddTask();

        task.setTaskType(taskTypeRepo.findById(dto.getTypeId()).orElseThrow());
        task.setTaskPriority(taskPriorityRepo.findById(dto.getPriorityId()).orElseThrow());
        task.setTaskStatus(taskStatusRepo.findById(dto.getStatusId()).orElseThrow());
        task.setTaskSubject(taskSubjectRepo.findById(dto.getSubjectId()).orElseThrow());
        task.setTaskProject(taskProjectRepo.findById(dto.getProjectId()).orElseThrow());
        task.setEmployee(employeeRepo.findById(dto.getEmployeeId()).orElseThrow());

        task.setTaskQuantity(dto.getTaskQuantity());
        task.setTaskDescription(dto.getTaskDescription());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());

        return task;
    }

}
