package com.cydeo.service;

import com.cydeo.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    List<UserDTO> listAllUsers();

    UserDTO findByUsername(String username);

    void saveUser(UserDTO userDTO);

    UserDTO update(UserDTO userDTO);

    @Transactional
    void deleteByUsername(String username);

    void deleteByUsernameBySettingFlag(String username);

    List<UserDTO> findManagers();

    List<UserDTO> findUsersByRoleDescription(String roleDescription);

}
