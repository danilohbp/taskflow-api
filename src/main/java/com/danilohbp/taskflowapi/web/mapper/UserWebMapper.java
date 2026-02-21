package com.danilohbp.taskflowapi.web.mapper;

import com.danilohbp.taskflowapi.application.query.user.dto.UserView;
import com.danilohbp.taskflowapi.application.usecase.user.create.CreateUserCommand;
import com.danilohbp.taskflowapi.web.request.user.CreateUserRequest;
import com.danilohbp.taskflowapi.web.response.user.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserWebMapper {

    public CreateUserCommand toCreateCommand(CreateUserRequest req) {
        if (req == null) throw new IllegalArgumentException("CreateUserRequest is required");
        return new CreateUserCommand(req.name(), req.email(), req.role());
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
