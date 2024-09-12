package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, UserServiceImpl userService, UserMapper userMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @Override
    public Optional<TaskDTO> findTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.map(taskMapper::mapToDTO);
    }

    @Override
    public List<TaskDTO> findAllTasks() {
       return taskRepository
               .findAll()
               .stream()
               .map(taskMapper::mapToDTO)
               .collect(Collectors.toList());
    }

    @Override
    public void saveTask(TaskDTO taskDto) {
        Task taskToBeSaved = taskMapper.mapToEntity(taskDto);
        if(taskToBeSaved.getTaskStatus() == null) {
            taskToBeSaved.setTaskStatus(Status.OPEN);
        }
        taskToBeSaved.setAssignedDate(LocalDate.now());
        taskRepository.save(taskToBeSaved);
    }

    @Override
    public void deleteTaskById(Long id) {
        Optional<Task> taskTobeDeleted = taskRepository.findById(id);
        if(taskTobeDeleted.isPresent()) {
            taskTobeDeleted.get().setIsDeleted(true);
            taskRepository.save(taskTobeDeleted.get());
        }
    }

    @Override
    public void updateTask(TaskDTO task) {
        Optional<Task> taskBeforeUpdate = taskRepository.findById(task.getId());
        Task taskAfterUpdate = taskMapper.mapToEntity(task);
        if(taskBeforeUpdate.isPresent()) {
            taskAfterUpdate.setTaskStatus(taskBeforeUpdate.get().getTaskStatus());
            taskAfterUpdate.setAssignedDate(taskBeforeUpdate.get().getAssignedDate());
            taskAfterUpdate.setId(taskBeforeUpdate.get().getId());
            taskRepository.save(taskAfterUpdate);
        }
    }

    @Override
    public List<TaskDTO> findAllTasksByProjectId(Long projectId) {
        return taskRepository
                .findAll()
                .stream()
                .filter(task -> task.getProject().getId().equals(projectId))
                .map(taskMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByProjectCode(String projectCode) {
        List<Task> deletedTaskList = taskRepository.findAllByProject_ProjectCode(projectCode)
                .stream()
                .map(task -> {
                    task.setIsDeleted(true);
                    return task;
                }).collect(Collectors.toList());

        taskRepository.saveAll(deletedTaskList);
    }


    @Override
    public List<TaskDTO> findAllTaskByStatusIsNotAndAssignedEmployee(Status status, String username) {
        User userInSession = userMapper.convertToUserEntity(userService.findByUsername(username));
        return taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status,userInSession)
                .stream()
                .map(taskMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTaskStatus(TaskDTO task) {
        Optional<Task> taskBeforeUpdate = taskRepository.findById(task.getId());
        if(taskBeforeUpdate.isPresent()) {
            TaskDTO taskToUpdate = taskMapper.mapToDTO(taskBeforeUpdate.get());
            taskToUpdate.setTaskStatus(task.getTaskStatus());
            taskToUpdate.setProject(task.getProject());
            taskToUpdate.setAssignedEmployee(task.getAssignedEmployee());
            taskRepository.save(taskMapper.mapToEntity(taskToUpdate));
        }
    }


    @Override
    public List<TaskDTO> findAllTasksByStatusAndEmployee(Status status, String username) {
        List<Task> taskList = taskRepository.findAllByTaskStatusAndAssignedEmployee_UserName(status, username);
        return taskList
                .stream()
                .map(taskMapper::mapToDTO)
                .collect(Collectors.toList());
    }

}
