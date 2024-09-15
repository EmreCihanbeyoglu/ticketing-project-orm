package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String createProject(Model model) {

        model.addAttribute("project", new ProjectDTO());
        model.addAttribute("projects", projectService.findAllProjects());
        model.addAttribute("managers", userService.findUsersByRoleDescription("Manager"));

        return "/project/create";
    }

    @PostMapping("/create")
    public String insertProject(@ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("projects", projectService.findAllProjects());
            model.addAttribute("managers", userService.findUsersByRoleDescription("Manager"));

            return "/project/create";

        }

        projectService.save(project);
        return "redirect:/project/create";

    }

    @GetMapping("/delete/{projectcode}")
    public String deleteProject(@PathVariable("projectcode") String projectcode) {
        projectService.deleteByProjectCode(projectcode);
        return "redirect:/project/create";
    }

    @GetMapping("/complete/{projectcode}")
    public String completeProject(@PathVariable("projectcode") String projectcode) {
        projectService.completeProject(projectcode);
        return "redirect:/project/create";
    }

    @GetMapping("/update/{projectcode}")
    public String editProject(@PathVariable("projectcode") String projectcode, Model model) {

        model.addAttribute("project", projectService.findProjectByCode(projectcode));
        model.addAttribute("projects", projectService.findAllProjects());
        model.addAttribute("managers", userService.findUsersByRoleDescription("Manager"));

        return "/project/update";

    }

    @PostMapping("/update")
    public String updateProject(@ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("projects", projectService.findAllProjects());
            model.addAttribute("managers", userService.findUsersByRoleDescription("Manager"));

            return "/project/update";

        }

        projectService.update(project);
        return "redirect:/project/create";

    }

    @GetMapping("/manager/project-status")
    public String getProjectByManager(Model model) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<ProjectDTO> projects = projectService.findAllProjectsByAssignedManager(username);
        model.addAttribute("projects", projects);
        return "/manager/project-status";
    }
//
//    @GetMapping("/manager/complete/{projectCode}")
//    public String managerCompleteProject(@PathVariable("projectCode") String projectCode) {
//        projectService.complete(projectService.findById(projectCode));
//        return "redirect:/project/manager/project-status";
//    }

}
