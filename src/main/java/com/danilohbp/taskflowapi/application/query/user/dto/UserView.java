package com.danilohbp.taskflowapi.application.query.user.dto;

import com.danilohbp.taskflowapi.domain.model.UserRole;

import java.time.LocalDateTime;

public record UserView(
        Long id,
        String name,
        String email,
        UserRole role,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
