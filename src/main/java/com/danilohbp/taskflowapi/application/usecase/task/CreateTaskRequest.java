package com.danilohbp.taskflowapi.application.usecase.task;

public record CreateTaskRequest(
    Long userId,
    String title,
    String description
) { }
