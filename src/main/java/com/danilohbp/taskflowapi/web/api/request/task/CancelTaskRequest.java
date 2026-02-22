package com.danilohbp.taskflowapi.web.api.request.task;

public record CancelTaskRequest(
        Long actorUserId,
        String reason
) {}
