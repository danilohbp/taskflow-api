package com.danilohbp.taskflowapi.application.usecase.user.create;

import com.danilohbp.taskflowapi.domain.model.UserRole;

public record CreateUserCommand(
        String name,
        String email,
        String rawPassword,
        UserRole role
) {}
