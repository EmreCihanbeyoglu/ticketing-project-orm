package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Spy
    @InjectMocks
    UserServiceImpl userService;




    @Test
    void deleteByUsernameBySettingFlag_test() {

//        when(userService.isSafeToDeleteUser(anyString())).thenReturn(false);
        doReturn(false).when(userService).isSafeToDeleteUser(anyString());

        userService.deleteByUsernameBySettingFlag(anyString());

        verify(userRepository, never()).save(any(User.class));

    }

}