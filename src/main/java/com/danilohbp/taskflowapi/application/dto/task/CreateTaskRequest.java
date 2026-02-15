package com.danilohbp.taskflowapi.application.dto.task;

public record CreateTaskRequest(
    Long userId,
    String title,
    String description
) { }
