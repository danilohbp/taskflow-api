package com.danilohbp.taskflowapi.web.api.controller;

import com.danilohbp.taskflowapi.application.usecase.user.create.CreateUserUseCase;
import com.danilohbp.taskflowapi.application.usecase.user.deactivate.DeactivateUserUseCase;
import com.danilohbp.taskflowapi.application.usecase.user.activate.ActivateUserUseCase;
import com.danilohbp.taskflowapi.application.usecase.user.update.UpdateUserUseCase;
import com.danilohbp.taskflowapi.domain.exception.NotFoundException;
import com.danilohbp.taskflowapi.infrastructure.persistence.query.UserQueryRepository;
import com.danilohbp.taskflowapi.web.mapper.UserWebMapper;
import com.danilohbp.taskflowapi.web.api.request.user.CreateUserRequest;
import com.danilohbp.taskflowapi.web.api.response.user.UserUpdatedResponse;
import com.danilohbp.taskflowapi.web.api.request.user.UpdateUserRequest;
import com.danilohbp.taskflowapi.web.api.response.user.UserCreatedResponse;
import com.danilohbp.taskflowapi.web.api.response.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "User management (CQRS light: commands for write, query repository for reads)")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final ActivateUserUseCase activateUserUseCase;
    private final DeactivateUserUseCase deactivateUserUseCase;

    private final UserQueryRepository userQueryRepository;
    private final UserWebMapper mapper;

    public UserController(
            CreateUserUseCase createUserUseCase,
            UpdateUserUseCase updateUserUseCase,
            ActivateUserUseCase activateUserUseCase,
            DeactivateUserUseCase deactivateUserUseCase,
            UserQueryRepository userQueryRepository,
            UserWebMapper mapper
    ) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.activateUserUseCase = activateUserUseCase;
        this.deactivateUserUseCase = deactivateUserUseCase;
        this.userQueryRepository = userQueryRepository;
        this.mapper = mapper;
    }

    // WRITE
    @Operation(
            summary = "Create a user",
            description = "Creates a new user. Email must be unique (case-insensitive)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreatedResponse create(@Valid @RequestBody CreateUserRequest request) {
        var result = createUserUseCase.execute(mapper.toCreateCommand(request));
        return new UserCreatedResponse(result.userId());
    }

    @Operation(
            summary = "Update a user",
            description = "Updates user name and/or email. Only provided fields are changed."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    @PutMapping("/{id}")
    public UserUpdatedResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        var result = updateUserUseCase.execute(mapper.toUpdateCommand(id, request));
        return new UserUpdatedResponse(result.userId());
    }

    @Operation(
            summary = "Activate a user",
            description = "Activates the user (idempotent). Returns 204 even if already active."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User activated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable Long id) {
        activateUserUseCase.execute(mapper.toActivateCommand(id));
    }

    @Operation(
            summary = "Deactivate a user",
            description = "Deactivates the user (idempotent). Returns 204 even if already inactive."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deactivated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        deactivateUserUseCase.execute(mapper.toDeactivateCommand(id));
    }

    // READ
    @Operation(
            summary = "List users",
            description = "Returns a list of users with role, active flag and audit timestamps."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping
    public List<UserResponse> list() {
        return userQueryRepository.listUsers()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // READ
    @Operation(
            summary = "Get user by id",
            description = "Returns a single user by id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        var view = userQueryRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return mapper.toResponse(view);
    }
}
