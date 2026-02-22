package com.danilohbp.taskflowapi.web.api.request.task;

public record AssignTaskRequest(
        Long actorUserId,
        Long assigneeId
) {}
