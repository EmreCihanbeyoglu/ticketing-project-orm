package com.cydeo.service;

import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Optional<TaskDTO> findTaskById(Long id);

    List<TaskDTO> findAllTasks();

    void saveTask(TaskDTO task);

    void deleteTaskById(Long id);

    void updateTask(TaskDTO task);

    List<TaskDTO> findAllTasksByProjectId(Long projectId);

    void deleteByProjectCode(String projectCode);

    List<TaskDTO> findAllTaskByStatusIsNotAndAssignedEmployee(Status status, String username);

    void updateTaskStatus(TaskDTO task);

    List<TaskDTO> findAllTasksByStatusAndEmployee(Status status, String username);

}
