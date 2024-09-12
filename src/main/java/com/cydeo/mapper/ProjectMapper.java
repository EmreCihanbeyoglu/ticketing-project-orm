package com.cydeo.mapper;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.PropertyResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.print.attribute.standard.Destination;

@Component
public class ProjectMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ProjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProjectDTO mapToProjectDTO(Project project) {

        return modelMapper.map(project, ProjectDTO.class);
    }

    public Project mapToProjectEntity(ProjectDTO projectDTO) {
        return modelMapper.map(projectDTO, Project.class);
    }

}

