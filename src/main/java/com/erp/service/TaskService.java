package com.erp.service;

import java.util.List;

import com.erp.dto.TaskRequestDto;
import com.erp.model.AddTask;

public interface TaskService {

    public AddTask createTask(TaskRequestDto dto);

    public List<AddTask> getAllTasks();

    public AddTask updateTask(int id, TaskRequestDto dto);

    public void deleteTask(int id);

    public AddTask getTaskById(int id);
}
