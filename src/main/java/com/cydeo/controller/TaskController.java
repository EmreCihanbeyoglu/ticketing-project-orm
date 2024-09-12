package com.cydeo.controller;

import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;

    public TaskController(TaskService taskService, ProjectService projectService, UserService userService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String createTask(Model model) {

        model.addAttribute("task", new TaskDTO());
        model.addAttribute("projects", projectService.findAllProjects());
        model.addAttribute("employees", userService.findUsersByRoleDescription("Employee"));
        model.addAttribute("tasks", taskService.findAllTasks());

        return "task/create";
    }

    @PostMapping("/create")
    public String insertTask(@Valid @ModelAttribute("task") TaskDTO task, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("projects", projectService.findAllProjects());
            model.addAttribute("employees", userService.findUsersByRoleDescription("Employee"));
            model.addAttribute("tasks", taskService.findAllTasks());

            return "/task/create";

        }

        taskService.saveTask(task);

        return "redirect:/task/create";
    }

    @GetMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.deleteTaskById(taskId);
        return "redirect:/task/create";
    }

    @GetMapping("/update/{taskId}")
    public String editTask(@PathVariable("taskId") Long taskId, Model model) {

        model.addAttribute("task", taskService.findTaskById(taskId).orElse(null));
        model.addAttribute("projects", projectService.findAllProjects());
        model.addAttribute("employees", userService.findUsersByRoleDescription("Employee"));
        model.addAttribute("tasks", taskService.findAllTasks());

        return "task/update";

    }

    @PostMapping("/update/{id}")
    public String updateTask(@ModelAttribute("task") TaskDTO task, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("projects", projectService.findAllProjects());
            model.addAttribute("employees", userService.findUsersByRoleDescription("Employee"));
            model.addAttribute("tasks", taskService.findAllTasks());

            return "/task/update";
        }

        taskService.updateTask(task);
        return "redirect:/task/create";
    }

    @GetMapping("/employee/pending-tasks")
    public String employeePendingTasks(Model model) {
        model.addAttribute("tasks", taskService.findAllTaskByStatusIsNotAndAssignedEmployee(Status.COMPLETE, "wudanesyha"));
        return "task/pending-tasks";
    }

    @GetMapping("/employee/edit/{id}")
    public String employeeEditTask(@PathVariable("id") Long id, Model model) {
        Optional<TaskDTO> task = taskService.findTaskById(id);
        task.ifPresent(taskDTO -> model.addAttribute("task", taskDTO));
        model.addAttribute("tasks", taskService.findAllTaskByStatusIsNotAndAssignedEmployee(Status.COMPLETE, "wudanesyha"));
        model.addAttribute("statuses", Status.values());

        return "task/status-update";

    }

    @PostMapping("/employee/update/{id}")
    public String employeeUpdateTask(@ModelAttribute("task") TaskDTO task, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("tasks", taskService.findAllTaskByStatusIsNotAndAssignedEmployee(Status.COMPLETE, "wudanesyha"));
            model.addAttribute("statuses", Status.values());

            return "/task/status-update";

        }

        taskService.updateTaskStatus(task);
        return "redirect:/task/employee/pending-tasks";

    }

    @GetMapping("/employee/archive")
    public String employeeArchivedTasks(Model model) {
        model.addAttribute("tasks", taskService.findAllTasksByStatusAndEmployee(Status.COMPLETE, "wudanesyha"));
        return "task/archive";
    }

}
