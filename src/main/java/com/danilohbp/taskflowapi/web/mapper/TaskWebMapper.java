package com.danilohbp.taskflowapi.web.mapper;

import com.danilohbp.taskflowapi.application.usecase.task.assign.AssignTaskCommand;
import com.danilohbp.taskflowapi.application.usecase.task.cancel.CancelTaskCommand;
import com.danilohbp.taskflowapi.application.usecase.task.complete.CompleteTaskCommand;
import com.danilohbp.taskflowapi.application.usecase.task.create.CreateTaskCommand;
import com.danilohbp.taskflowapi.application.usecase.task.create.CreateTaskResult;
import com.danilohbp.taskflowapi.application.usecase.task.start.StartTaskCommand;
import com.danilohbp.taskflowapi.domain.model.TaskPriority;
import com.danilohbp.taskflowapi.infrastructure.persistence.query.dto.TaskListItemView;
import com.danilohbp.taskflowapi.web.api.response.task.TaskCreatedResponse;
import com.danilohbp.taskflowapi.web.api.response.task.TaskResponse;
import com.danilohbp.taskflowapi.web.api.request.task.AssignTaskRequest;
import com.danilohbp.taskflowapi.web.api.request.task.CancelTaskRequest;
import com.danilohbp.taskflowapi.web.api.request.task.CompleteTaskRequest;
import com.danilohbp.taskflowapi.web.api.request.task.CreateTaskRequest;
import com.danilohbp.taskflowapi.web.api.request.task.StartTaskRequest;
import com.danilohbp.taskflowapi.web.view.request.CreateTaskForm;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
public class TaskWebMapper {

    public CreateTaskCommand toCreateCommand(CreateTaskRequest req) {
        if (req == null) throw new IllegalArgumentException("CreateTaskRequest is required");

        // Padrão recomendado no início: actor é o reporter.
        // Se um dia você permitir "criar em nome de outro", você adiciona reporterId no request.
        return new CreateTaskCommand(
                req.actorUserId(), // actorUserId
                req.actorUserId(), // reporterId (por enquanto igual ao actor)
                req.assigneeId(),
                req.title(),
                req.description(),
                req.priority(),
                req.dueDate()
        );
    }

    public AssignTaskCommand toAssignCommand(Long taskId, AssignTaskRequest req) {
        if (req == null) throw new IllegalArgumentException("AssignTaskRequest is required");

        return new AssignTaskCommand(
                req.actorUserId(),
                taskId,
                req.assigneeId()
        );
    }

    public StartTaskCommand toStartCommand(Long taskId, StartTaskRequest req) {
        if (req == null) throw new IllegalArgumentException("StartTaskRequest is required");

        return new StartTaskCommand(
                req.actorUserId(),
                taskId
        );
    }

    public CompleteTaskCommand toCompleteCommand(Long taskId, CompleteTaskRequest req) {
        if (req == null) throw new IllegalArgumentException("CompleteTaskRequest is required");

        return new CompleteTaskCommand(
                req.actorUserId(),
                taskId
        );
    }

    public CancelTaskCommand toCancelCommand(Long taskId, CancelTaskRequest req) {
        if (req == null) throw new IllegalArgumentException("CancelTaskRequest is required");

        return new CancelTaskCommand(
                req.actorUserId(),
                taskId,
                req.reason()
        );
    }

    public TaskCreatedResponse toCreatedResponse(CreateTaskResult result) {
        if (result == null) throw new IllegalArgumentException("CreateTaskResult is required");
        return new TaskCreatedResponse(result.taskId());
    }

    public CreateTaskCommand toCreateCommand(CreateTaskForm form, Long actorUserId) {
        if (form == null) throw new IllegalArgumentException("CreateTaskForm is required");
        if (actorUserId == null) throw new IllegalArgumentException("actorUserId is required");

        TaskPriority priority = parsePriority(form.getPriority());
        LocalDate dueDate = parseDueDate(form.getDueDate());

        Long reporterId = form.getReporterId() != null ? form.getReporterId() : actorUserId;

        return new CreateTaskCommand(
                actorUserId,
                reporterId,
                form.getAssigneeId(),
                form.getTitle(),
                form.getDescription(),
                priority,
                dueDate.atStartOfDay()
        );
    }

    private TaskPriority parsePriority(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return TaskPriority.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Prioridade inválida: " + value);
        }
    }

    private LocalDate parseDueDate(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Data inválida (use AAAA-MM-DD): " + value);
        }
    }

    public TaskResponse toResponse(TaskListItemView view) {
        if (view == null) throw new IllegalArgumentException("TaskListItemView is required");

        return new TaskResponse(
                view.id(),
                view.title(),
                null,
                view.status(),
                view.priority(),
                view.dueDate(),
                view.assigneeName(),
                view.reporterName()
        );
    }
}
