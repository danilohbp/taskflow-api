package com.danilohbp.taskflowapi.web.request.task;

public record AssignTaskRequest(
        Long actorUserId,
        Long assigneeId
) {}
