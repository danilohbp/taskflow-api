package com.danilohbp.taskflowapi.application.usecase.user.deactivate;

import jakarta.validation.constraints.NotNull;

public record DeactivateUserCommand(
        @NotNull Long userId
) {}
