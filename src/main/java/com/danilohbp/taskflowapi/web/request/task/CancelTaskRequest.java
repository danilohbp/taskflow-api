package com.danilohbp.taskflowapi.web.request.task;

public record CancelTaskRequest(
        Long actorUserId,
        String reason
) {}
