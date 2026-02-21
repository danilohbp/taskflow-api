package com.danilohbp.taskflowapi.application.usecase.task;

import com.danilohbp.taskflowapi.domain.model.TaskStatus;
import com.danilohbp.taskflowapi.domain.validation.NullOrNotBlank;

public record UpdateTaskRequest(
    @NullOrNotBlank(message = "Title não pode ser vazio.")
    String title,

    @NullOrNotBlank(message = "Description não pode ser vazio.")
    String description,
    TaskStatus status
) { }
