package com.danilohbp.taskflowapi.application.usecase.user.activate;

import jakarta.validation.constraints.NotNull;

public record ActivateUserCommand(
        @NotNull Long userId
) {}
