package com.danilohbp.taskflowapi.application.usecase.task.assign;

public record AssignTaskCommand(
        Long actorUserId,
        Long taskId,
        Long assigneeId // pode ser null para "desatribuir"
) {}
