package com.danilohbp.taskflowapi.web.request.task;

import com.danilohbp.taskflowapi.domain.model.TaskPriority;

import java.time.LocalDateTime;

public record CreateTaskRequest(
        Long actorUserId,
        Long assigneeId,
        String title,
        String description,
        TaskPriority priority,
        LocalDateTime dueDate
) {}
