package com.cydeo.repository;

import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByProject_ProjectCode(String projectCode);

    List<Task> findAllByTaskStatusIsNotAndAssignedEmployee(Status status, User assignedEmployee);

    List<Task> findAllByTaskStatusAndAssignedEmployee_UserName(Status status, String userName);
}
