package com.danilohbp.taskflowapi.web.controller;

import com.danilohbp.taskflowapi.application.usecase.user.create.CreateUserUseCase;
import com.danilohbp.taskflowapi.domain.exception.NotFoundException;
import com.danilohbp.taskflowapi.infrastructure.persistence.query.UserQueryRepository;
import com.danilohbp.taskflowapi.web.mapper.UserWebMapper;
import com.danilohbp.taskflowapi.web.request.user.CreateUserRequest;
import com.danilohbp.taskflowapi.web.response.user.UserCreatedResponse;
import com.danilohbp.taskflowapi.web.response.user.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UserQueryRepository userQueryRepository;
    private final UserWebMapper mapper;

    public UserController(
            CreateUserUseCase createUserUseCase,
            UserQueryRepository userQueryRepository,
            UserWebMapper mapper
    ) {
        this.createUserUseCase = createUserUseCase;
        this.userQueryRepository = userQueryRepository;
        this.mapper = mapper;
    }

    // WRITE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreatedResponse create(@Valid @RequestBody CreateUserRequest request) {
        var result = createUserUseCase.execute(mapper.toCreateCommand(request));
        return new UserCreatedResponse(result.userId());
    }

    // READ
    @GetMapping
    public List<UserResponse> list() {
        return userQueryRepository.listUsers()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // READ
    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        var view = userQueryRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return mapper.toResponse(view);
    }
}
