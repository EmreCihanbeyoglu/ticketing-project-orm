package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {


    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final TaskService taskService;
    private final UserMapper userMapper;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, @Lazy TaskService taskService, @Lazy UserService userService, UserMapper userMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.taskService = taskService;
        this.userMapper = userMapper;
    }

    @Override
    public List<ProjectDTO> findAllProjects() {
        return projectRepository.findAll().stream().map(projectMapper::mapToProjectDTO).collect(Collectors.toList());
    }

    @Override
    public ProjectDTO findProjectByCode(String projectCode) {
        return projectMapper.mapToProjectDTO(projectRepository.findByProjectCode(projectCode));
    }

    @Override
    public void save(ProjectDTO projectDto) {
        Project projectToBeSaved = projectMapper.mapToProjectEntity(projectDto);
        projectToBeSaved.setProjectStatus(Status.OPEN);
//        Long assignedManagerId = userRepository.findByUserName(projectToBeSaved.getAssignedManager().getUserName()).getId();
//        projectToBeSaved.getAssignedManager().setId(assignedManagerId);
        projectRepository.save(projectToBeSaved);
    }

    @Override
    public void deleteByProjectCode(String projectCode) {
        Project projectToBeDeleted = projectRepository.findByProjectCode(projectCode);
        projectToBeDeleted.setIsDeleted(true);
        // to be able to use the same project code, I set the deleted one to another value
        projectToBeDeleted.setProjectCode(projectToBeDeleted.getProjectCode() + "-" + projectToBeDeleted.getId());

        taskService.deleteByProjectCode(projectCode);


        projectRepository.save(projectToBeDeleted);
    }

    @Override
    public void completeProject(String projectCode) {
        Project projectToBeCompleted = projectRepository.findByProjectCode(projectCode);
        projectToBeCompleted.setProjectStatus(Status.COMPLETE);
        projectRepository.save(projectToBeCompleted);
    }

    @Override
    public void update(ProjectDTO projectDto) {
        Project projectBeforeUpdate = projectRepository.findByProjectCode(projectDto.getProjectCode());
        Project projectAfterUpdate = projectMapper.mapToProjectEntity(projectDto);
        projectAfterUpdate.setProjectStatus(projectBeforeUpdate.getProjectStatus());
        projectAfterUpdate.setId(projectBeforeUpdate.getId());
        projectRepository.save(projectAfterUpdate);
    }

    @Override
    public List<ProjectDTO> findAllProjectsByAssignedManager(String assignedManagerUserName) {
        User user = userMapper.convertToUserEntity(userService.findByUsername(assignedManagerUserName));
        List<Project> managerProjectList = projectRepository.findAllByAssignedManager(user);
        return managerProjectList.stream().map(project -> {
                    ProjectDTO projectDTO = projectMapper.mapToProjectDTO(project);
                    Long countOfUnfinishedTasks = taskService.findAllTasksByProjectId(projectDTO.getId()).stream().filter(task -> task.getTaskStatus().equals(Status.OPEN)).count();
                    Long countOfCompletedTasks = taskService.findAllTasksByProjectId(projectDTO.getId()).stream().filter(task -> !task.getTaskStatus().equals(Status.OPEN)).count();
                    projectDTO.setUnfinishedTaskCounts(countOfUnfinishedTasks);
                    projectDTO.setCompleteTaskCounts(countOfCompletedTasks);
                    return projectDTO;
        }).collect(Collectors.toList());
    }
}
