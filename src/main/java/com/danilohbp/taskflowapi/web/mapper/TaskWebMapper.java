package com.danilohbp.taskflowapi.web.mapper;

import com.danilohbp.taskflowapi.application.usecase.task.assign.AssignTaskCommand;
import com.danilohbp.taskflowapi.application.usecase.task.cancel.CancelTaskCommand;
import com.danilohbp.taskflowapi.application.usecase.task.complete.CompleteTaskCommand;
import com.danilohbp.taskflowapi.application.usecase.task.create.CreateTaskCommand;
import com.danilohbp.taskflowapi.application.usecase.task.create.CreateTaskResult;
import com.danilohbp.taskflowapi.application.usecase.task.start.StartTaskCommand;
import com.danilohbp.taskflowapi.web.request.task.AssignTaskRequest;
import com.danilohbp.taskflowapi.web.request.task.CancelTaskRequest;
import com.danilohbp.taskflowapi.web.request.task.CompleteTaskRequest;
import com.danilohbp.taskflowapi.web.request.task.CreateTaskRequest;
import com.danilohbp.taskflowapi.web.request.task.StartTaskRequest;
import com.danilohbp.taskflowapi.web.response.task.TaskCreatedResponse;
import org.springframework.stereotype.Component;

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
}
