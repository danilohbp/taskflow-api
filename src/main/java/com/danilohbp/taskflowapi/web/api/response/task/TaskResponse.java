package com.danilohbp.taskflowapi.web.api.response.task;

import com.danilohbp.taskflowapi.domain.model.TaskPriority;
import com.danilohbp.taskflowapi.domain.model.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime dueDate,
        String assigneeName,
        String reporterName
) {}
