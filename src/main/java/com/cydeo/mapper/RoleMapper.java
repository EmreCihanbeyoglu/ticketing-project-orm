package com.cydeo.mapper;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public RoleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Role convertToRole(RoleDTO roleDTO) {
        return modelMapper.map(roleDTO, Role.class);
    }

    public RoleDTO convertToRoleDto(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

}
