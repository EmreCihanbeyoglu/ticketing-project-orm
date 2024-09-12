package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {

    List<ProjectDTO> findAllProjects();

    ProjectDTO findProjectByCode(String projectCode);

    void save(ProjectDTO project);

    void deleteByProjectCode(String projectCode);

    void completeProject(String projectCode);

    void update(ProjectDTO project);

    List<ProjectDTO> findAllProjectsByAssignedManager(String assignedManager);
}
