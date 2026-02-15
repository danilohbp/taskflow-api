package com.danilohbp.taskflowapi.web.controller;

import com.danilohbp.taskflowapi.application.dto.task.CreateTaskRequest;
import com.danilohbp.taskflowapi.application.dto.task.TaskResponse;
import com.danilohbp.taskflowapi.application.dto.task.UpdateTaskRequest;
import com.danilohbp.taskflowapi.application.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.create(request);
    }

    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PatchMapping("/{id}")
    public TaskResponse updateById(
        @PathVariable Long id,
        @RequestBody @Valid UpdateTaskRequest request
    ) {
        return taskService.updateById(id, request);
    }

    @GetMapping
    public List<TaskResponse> list() {
        return taskService.listAll();
    }
}
