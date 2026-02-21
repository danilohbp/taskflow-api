package com.danilohbp.taskflowapi.application.usecase.task;

import com.danilohbp.taskflowapi.domain.model.TaskStatus;

public record TaskResponse(
    Long id,
    Long userId,
    String title,
    String description,
    TaskStatus status
) { }
