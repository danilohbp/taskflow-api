package com.danilohbp.taskflowapi.web.api.controller;

import com.danilohbp.taskflowapi.application.usecase.task.assign.AssignTaskUseCase;
import com.danilohbp.taskflowapi.application.usecase.task.cancel.CancelTaskUseCase;
import com.danilohbp.taskflowapi.application.usecase.task.complete.CompleteTaskUseCase;
import com.danilohbp.taskflowapi.application.usecase.task.create.CreateTaskUseCase;
import com.danilohbp.taskflowapi.application.usecase.task.start.StartTaskUseCase;
import com.danilohbp.taskflowapi.infrastructure.persistence.query.TaskQueryRepository;
import com.danilohbp.taskflowapi.web.mapper.TaskWebMapper;
import com.danilohbp.taskflowapi.web.api.request.task.*;
import com.danilohbp.taskflowapi.web.api.response.task.TaskCreatedResponse;
import com.danilohbp.taskflowapi.web.api.response.task.TaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tasks", description = "Task lifecycle management (create, assign, start, complete, cancel)")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final AssignTaskUseCase assignTaskUseCase;
    private final StartTaskUseCase startTaskUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;
    private final CancelTaskUseCase cancelTaskUseCase;

    private final TaskQueryRepository taskQueryRepository;
    private final TaskWebMapper mapper;

    public TaskController(
            CreateTaskUseCase createTaskUseCase,
            AssignTaskUseCase assignTaskUseCase,
            StartTaskUseCase startTaskUseCase,
            CompleteTaskUseCase completeTaskUseCase,
            CancelTaskUseCase cancelTaskUseCase,
            TaskQueryRepository taskQueryRepository,
            TaskWebMapper mapper
    ) {
        this.createTaskUseCase = createTaskUseCase;
        this.assignTaskUseCase = assignTaskUseCase;
        this.startTaskUseCase = startTaskUseCase;
        this.completeTaskUseCase = completeTaskUseCase;
        this.cancelTaskUseCase = cancelTaskUseCase;
        this.taskQueryRepository = taskQueryRepository;
        this.mapper = mapper;
    }

    // WRITE

    @Operation(
            summary = "Create task",
            description = "Creates a new task with initial status OPEN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskCreatedResponse create(@RequestBody CreateTaskRequest request) {
        var result = createTaskUseCase.execute(mapper.toCreateCommand(request));
        return mapper.toCreatedResponse(result);
    }

    @Operation(
            summary = "Assign task",
            description = "Assigns a task to a user. Returns 204 on success."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task assigned"),
            @ApiResponse(responseCode = "400", description = "Invalid transition / validation error"),
            @ApiResponse(responseCode = "404", description = "Task or user not found"),
            @ApiResponse(responseCode = "409", description = "Business rule violation")
    })
    @PostMapping("/{taskId}/assign")
    public ResponseEntity<Void> assign(
            @Parameter(description = "Task id", example = "100")
            @PathVariable Long taskId,
            @RequestBody AssignTaskRequest request
    ) {
        assignTaskUseCase.execute(mapper.toAssignCommand(taskId, request));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Start task",
            description = "Moves task status from OPEN to IN_PROGRESS. Returns 204 on success."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task started"),
            @ApiResponse(responseCode = "400", description = "Invalid state transition / validation error"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PostMapping("/{taskId}/start")
    public ResponseEntity<Void> start(
            @Parameter(description = "Task id", example = "100")
            @PathVariable Long taskId,
            @RequestBody StartTaskRequest request
    ) {
        startTaskUseCase.execute(mapper.toStartCommand(taskId, request));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Complete task",
            description = "Marks a task as COMPLETED. Only allowed if task is IN_PROGRESS. Returns 204 on success."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task completed"),
            @ApiResponse(responseCode = "400", description = "Invalid state transition / validation error"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PostMapping("/{taskId}/complete")
    public ResponseEntity<Void> complete(
            @Parameter(description = "Task id", example = "100")
            @PathVariable Long taskId,
            @RequestBody CompleteTaskRequest request
    ) {
        completeTaskUseCase.execute(mapper.toCompleteCommand(taskId, request));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Cancel task",
            description = "Cancels a task. Cannot cancel a completed task. Returns 204 on success."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task cancelled"),
            @ApiResponse(responseCode = "400", description = "Invalid state transition / validation error"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PostMapping("/{taskId}/cancel")
    public ResponseEntity<Void> cancel(
            @Parameter(description = "Task id", example = "100")
            @PathVariable Long taskId,
            @RequestBody CancelTaskRequest request
    ) {
        cancelTaskUseCase.execute(mapper.toCancelCommand(taskId, request));
        return ResponseEntity.noContent().build();
    }

    // READ
    @Operation(
            summary = "List tasks",
            description = "Returns tasks ordered by updatedAt desc, including assignee and reporter names."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping
    public List<TaskResponse> list() {
        return taskQueryRepository.listForTasksPage()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
