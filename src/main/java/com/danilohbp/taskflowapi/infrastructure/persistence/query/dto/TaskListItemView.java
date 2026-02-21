package com.danilohbp.taskflowapi.infrastructure.persistence.query.dto;

import com.danilohbp.taskflowapi.domain.model.TaskPriority;
import com.danilohbp.taskflowapi.domain.model.TaskStatus;

import java.time.LocalDateTime;

public record TaskListItemView(
        Long id,
        String title,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime dueDate,
        Long assigneeId,
        String assigneeName,
        Long reporterId,
        String reporterName
) { }
