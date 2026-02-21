package com.danilohbp.taskflowapi.application.usecase.task.start;

public record StartTaskCommand(
        Long actorUserId,
        Long taskId
) {}
