package com.danilohbp.taskflowapi.web.view;

import com.danilohbp.taskflowapi.application.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TasksPageController {

    private final TaskService taskService;

    public TasksPageController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        model.addAttribute("tasks", taskService.listAll());
        return "tasks";
    }
}
