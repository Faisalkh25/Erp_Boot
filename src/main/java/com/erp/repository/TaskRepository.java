package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.AddTask;

public interface TaskRepository extends JpaRepository<AddTask, Integer> {

}
