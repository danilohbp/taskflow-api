package com.danilohbp.taskflowapi.application.dto.user;

import com.danilohbp.taskflowapi.domain.model.TaskStatus;

import java.util.UUID;

public record UserResponse(
    Long id,
    String name,
    String email
) { }
