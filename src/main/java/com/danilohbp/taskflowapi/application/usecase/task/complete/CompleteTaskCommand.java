package com.danilohbp.taskflowapi.application.usecase.task.complete;

public record CompleteTaskCommand(
        Long actorUserId,
        Long taskId
) {}
