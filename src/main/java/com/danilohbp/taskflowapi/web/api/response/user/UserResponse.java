package com.danilohbp.taskflowapi.web.api.response.user;

import com.danilohbp.taskflowapi.domain.model.UserRole;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        UserRole role,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
