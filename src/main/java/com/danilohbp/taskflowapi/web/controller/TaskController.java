package com.danilohbp.taskflowapi.web.controller;

import com.danilohbp.taskflowapi.application.usecase.task.assign.AssignTaskUseCase;
import com.danilohbp.taskflowapi.application.usecase.task.cancel.CancelTaskUseCase;
import com.danilohbp.taskflowapi.application.usecase.task.complete.CompleteTaskUseCase;
import com.danilohbp.taskflowapi.application.usecase.task.create.CreateTaskUseCase;
import com.danilohbp.taskflowapi.application.usecase.task.start.StartTaskUseCase;
import com.danilohbp.taskflowapi.web.mapper.TaskWebMapper;
import com.danilohbp.taskflowapi.web.request.task.AssignTaskRequest;
import com.danilohbp.taskflowapi.web.request.task.CancelTaskRequest;
import com.danilohbp.taskflowapi.web.request.task.CompleteTaskRequest;
import com.danilohbp.taskflowapi.web.request.task.CreateTaskRequest;
import com.danilohbp.taskflowapi.web.request.task.StartTaskRequest;
import com.danilohbp.taskflowapi.web.response.task.TaskCreatedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final AssignTaskUseCase assignTaskUseCase;
    private final StartTaskUseCase startTaskUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;
    private final CancelTaskUseCase cancelTaskUseCase;

    private final TaskWebMapper mapper;

    public TaskController(
            CreateTaskUseCase createTaskUseCase,
            AssignTaskUseCase assignTaskUseCase,
            StartTaskUseCase startTaskUseCase,
            CompleteTaskUseCase completeTaskUseCase,
            CancelTaskUseCase cancelTaskUseCase,
            TaskWebMapper mapper
    ) {
        this.createTaskUseCase = createTaskUseCase;
        this.assignTaskUseCase = assignTaskUseCase;
        this.startTaskUseCase = startTaskUseCase;
        this.completeTaskUseCase = completeTaskUseCase;
        this.cancelTaskUseCase = cancelTaskUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<TaskCreatedResponse> create(@RequestBody CreateTaskRequest request) {
        var result = createTaskUseCase.execute(mapper.toCreateCommand(request));
        return ResponseEntity.ok(mapper.toCreatedResponse(result));
    }

    @PostMapping("/{taskId}/assign")
    public ResponseEntity<Void> assign(@PathVariable Long taskId, @RequestBody AssignTaskRequest request) {
        assignTaskUseCase.execute(mapper.toAssignCommand(taskId, request));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/start")
    public ResponseEntity<Void> start(@PathVariable Long taskId, @RequestBody StartTaskRequest request) {
        startTaskUseCase.execute(mapper.toStartCommand(taskId, request));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<Void> complete(@PathVariable Long taskId, @RequestBody CompleteTaskRequest request) {
        completeTaskUseCase.execute(mapper.toCompleteCommand(taskId, request));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long taskId, @RequestBody CancelTaskRequest request) {
        cancelTaskUseCase.execute(mapper.toCancelCommand(taskId, request));
        return ResponseEntity.noContent().build();
    }
}
