package com.danilohbp.taskflowapi.web.view.controller;

import com.danilohbp.taskflowapi.application.usecase.task.create.CreateTaskCommand;
import com.danilohbp.taskflowapi.application.usecase.task.create.CreateTaskUseCase;
import com.danilohbp.taskflowapi.domain.model.TaskPriority;
import com.danilohbp.taskflowapi.infrastructure.persistence.query.TaskQueryRepository;
import com.danilohbp.taskflowapi.infrastructure.persistence.query.UserQueryRepository;
import com.danilohbp.taskflowapi.infrastructure.security.CustomUserDetails;
import com.danilohbp.taskflowapi.web.mapper.TaskWebMapper;
import com.danilohbp.taskflowapi.web.view.request.CreateTaskForm;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class TasksPageController {

    private final TaskQueryRepository taskQueryRepository;
    private final UserQueryRepository userQueryRepository;
    private final CreateTaskUseCase createTaskUseCase;
    private final TaskWebMapper mapper;

    public TasksPageController(
            TaskQueryRepository taskQueryRepository,
            UserQueryRepository userQueryRepository,
            CreateTaskUseCase createTaskUseCase,
            TaskWebMapper mapper
    ) {
        this.taskQueryRepository = taskQueryRepository;
        this.userQueryRepository = userQueryRepository;
        this.createTaskUseCase = createTaskUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/tasks")
    public String page(Model model) {
        model.addAttribute("tasks", taskQueryRepository.listForTasksPage());
        model.addAttribute("users", userQueryRepository.listUsers()); // para dropdowns
        model.addAttribute("form", new CreateTaskForm());
        return "tasks";
    }

    @PostMapping("/tasks")
    public String create(@Valid @ModelAttribute("form") CreateTaskForm form,
                         BindingResult binding,
                         Model model,
                         @AuthenticationPrincipal CustomUserDetails principal) {

        if (binding.hasErrors()) {
            model.addAttribute("tasks", taskQueryRepository.listForTasksPage());
            model.addAttribute("users", userQueryRepository.listUsers());
            return "tasks";
        }

        Long actorUserId = principal.getId();

        CreateTaskCommand command = mapper.toCreateCommand(form, actorUserId);
        createTaskUseCase.execute(command);

        return "redirect:/tasks";
    }
}
