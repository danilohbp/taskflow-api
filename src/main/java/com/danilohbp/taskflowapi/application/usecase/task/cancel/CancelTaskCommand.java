package com.danilohbp.taskflowapi.application.usecase.task.cancel;

public record CancelTaskCommand(
        Long actorUserId,
        Long taskId,
        String reason // opcional (pode ser null)
) {}
