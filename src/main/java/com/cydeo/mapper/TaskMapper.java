package com.cydeo.mapper;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public TaskMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Task mapToEntity(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, Task.class);
    }

    public TaskDTO mapToDTO(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }

}
