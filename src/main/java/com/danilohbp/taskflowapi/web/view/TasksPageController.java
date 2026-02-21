package com.danilohbp.taskflowapi.web.view;

import com.danilohbp.taskflowapi.infrastructure.persistence.query.TaskQueryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TasksPageController {

    private final TaskQueryRepository taskQueryRepository;

    public TasksPageController(TaskQueryRepository taskQueryRepository) {
        this.taskQueryRepository = taskQueryRepository;
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        model.addAttribute("tasks", taskQueryRepository.listForTasksPage());
        return "tasks";
    }
}
