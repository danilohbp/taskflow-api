package com.danilohbp.taskflowapi.web.mapper;

import com.danilohbp.taskflowapi.application.query.user.dto.UserView;
import com.danilohbp.taskflowapi.application.usecase.user.activate.ActivateUserCommand;
import com.danilohbp.taskflowapi.application.usecase.user.create.CreateUserCommand;
import com.danilohbp.taskflowapi.application.usecase.user.deactivate.DeactivateUserCommand;
import com.danilohbp.taskflowapi.application.usecase.user.update.UpdateUserCommand;
import com.danilohbp.taskflowapi.web.api.request.user.CreateUserRequest;
import com.danilohbp.taskflowapi.web.api.request.user.UpdateUserRequest;
import com.danilohbp.taskflowapi.web.api.response.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserWebMapper {

    public CreateUserCommand toCreateCommand(CreateUserRequest req) {
        if (req == null) throw new IllegalArgumentException("CreateUserRequest is required");
        return new CreateUserCommand(req.name(), req.email(), req.password(), req.role());
    }

    public UpdateUserCommand toUpdateCommand(Long userId, UpdateUserRequest req) {
        if (userId == null) throw new IllegalArgumentException("userId is required");
        if (req == null) throw new IllegalArgumentException("UpdateUserRequest is required");

        return new UpdateUserCommand(userId, req.name(), req.email());
    }

    public ActivateUserCommand toActivateCommand(Long userId) {
        if (userId == null) throw new IllegalArgumentException("userId is required");
        return new ActivateUserCommand(userId);
    }

    public DeactivateUserCommand toDeactivateCommand(Long userId) {
        if (userId == null) throw new IllegalArgumentException("userId is required");
        return new DeactivateUserCommand(userId);
    }

    public UserResponse toResponse(UserView view) {
        if (view == null) throw new IllegalArgumentException("UserView is required");
        return new UserResponse(
                view.id(),
                view.name(),
                view.email(),
                view.role(),
                view.active(),
                view.createdAt(),
                view.updatedAt()
        );
    }
}
