package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskServiceImpl taskService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ProjectService projectService, @Lazy TaskServiceImpl taskService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> listAllUsers() {
       return userRepository.findAll().stream().map(userMapper::convertToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUsername(String username) {
        return userMapper.convertToUserDto(userRepository.findByUserName(username));
    }

    @Override
    public void saveUser(UserDTO userDTO) {
        User user = userMapper.convertToUserEntity(userDTO);
        user.setPassWord(passwordEncoder.encode(user.getPassWord()));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        Long userId = userRepository.findByUserName(userDTO.getUserName()).getId();
        User updatedUser = userMapper.convertToUserEntity(userDTO);
        updatedUser.setId(userId);
        userRepository.save(updatedUser);
        return findByUsername(updatedUser.getUserName());
    }

    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteByUserName(username);
    }


    /**
     * In the real applications, it is not common
     * to delete a record from database. Instead, a
     * flag can be used to specify if the record
     * is in life. When you use this strategy, in each
     * query to database should be updated by WHERE
     * is_deleted = false. You can do it in the entity
     * class level so that it will be attached to each
     * query for this class automatically
     * @param username
     */
    @Override
    public void deleteByUsernameBySettingFlag(String username) {
        User user = userRepository.findByUserName(username);
        if(isSafeToDeleteUser(username)) {
            user.setIsDeleted(true);
            // to be able to use the same username for another user after deleting this, we can
            // change the username to something ese.
            user.setUserName(user.getUserName() + " - " + user.getId());
            userRepository.save(user);
        }
    }

    private boolean isSafeToDeleteUser(String username) {
       User user =  userRepository.findByUserName(username);
       if(user.getRole().getDescription().equals("Manager")) {
           List<ProjectDTO> projectList =  projectService.findAllProjectsByAssignedManager(user.getUserName());
           return projectList.stream().allMatch(project -> project.getProjectStatus().equals(Status.COMPLETE));
       }
       if(user.getRole().getDescription().equals("Employee")) {
           List<TaskDTO> taskList = taskService.findAllTaskByStatusIsNotAndAssignedEmployee(Status.COMPLETE, user.getUserName());
           return taskList.isEmpty();
       }
        return false;
    }

    @Override
    public List<UserDTO> findManagers() {
        return userRepository.fetchManagers().stream().map(userMapper::convertToUserDto).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> findUsersByRoleDescription(String roleDescription) {

        List<User> userList = userRepository.findAllByRole_DescriptionIgnoreCase(roleDescription);
        return userList.stream().map(userMapper::convertToUserDto).collect(Collectors.toList());
    }
}
