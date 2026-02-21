package com.danilohbp.taskflowapi.application.usecase.task.create;

import com.danilohbp.taskflowapi.domain.model.TaskPriority;

import java.time.LocalDateTime;

public record CreateTaskCommand(
        Long actorUserId,      // quem está criando (normalmente reporter)
        Long reporterId,       // se você quiser permitir criar em nome de outro, senão pode remover
        Long assigneeId,       // opcional
        String title,
        String description,
        TaskPriority priority,
        LocalDateTime dueDate
) {}
