package com.danilohbp.taskflowapi.web.api.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 1, max = 120) String name,
        @Email @Size(max = 180) String email
) {}
